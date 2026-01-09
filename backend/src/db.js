const fs = require('fs');
const path = require('path');
const sqlite3 = require('sqlite3').verbose();
const { createClient } = require('@libsql/client');

// Se as variáveis de ambiente do Turso estiverem definidas, usamos o banco remoto
const useTurso = Boolean(process.env.TURSO_URL && process.env.TURSO_AUTH_TOKEN);

let db = null;
let client = null;
let schemaInitialized = false;

if (useTurso) {
  const migrationBypassFetch = async (input, init) => {
    const target = typeof input === 'string' ? input : input?.url || '';
    if (target.includes('/v1/jobs')) {
      return new Response(JSON.stringify({ error: 'migrations disabled' }), {
        status: 404,
        headers: { 'Content-Type': 'application/json' },
      });
    }

    return fetch(input, init);
  };

  client = createClient({
    url: process.env.TURSO_URL,
    authToken: process.env.TURSO_AUTH_TOKEN,
    fetch: migrationBypassFetch,
  });

  // Inicializar schema no Turso (caso ainda não exista)
  (async () => {
    try {
      await client.execute(
        `CREATE TABLE IF NOT EXISTS devices (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          name TEXT NOT NULL,
          sim_number TEXT,
          data_limit_mb INTEGER,
          created_at TEXT NOT NULL DEFAULT (datetime('now'))
        )`
      );

      await client.execute(
        `CREATE TABLE IF NOT EXISTS usage_logs (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          device_id INTEGER NOT NULL,
          megabytes REAL NOT NULL,
          network_type TEXT,
          description TEXT,
          recorded_at TEXT NOT NULL,
          created_at TEXT NOT NULL DEFAULT (datetime('now')),
          FOREIGN KEY(device_id) REFERENCES devices(id) ON DELETE CASCADE
        )`
      );

      schemaInitialized = true;
    } catch (err) {
      console.error('Failed to initialize Turso schema', err);
    }
  })();
} else {
  const dataDir = path.join(__dirname, '..', 'data');
  if (!fs.existsSync(dataDir)) {
    fs.mkdirSync(dataDir, { recursive: true });
  }

  const dbPath = path.join(dataDir, 'monitor.db');
  db = new sqlite3.Database(dbPath);

  db.serialize(() => {
    db.run('PRAGMA foreign_keys = ON');

    db.run(
      `CREATE TABLE IF NOT EXISTS devices (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        sim_number TEXT,
        data_limit_mb INTEGER,
        created_at TEXT NOT NULL DEFAULT (datetime('now'))
      )`
    );

    db.run(
      `CREATE TABLE IF NOT EXISTS usage_logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        device_id INTEGER NOT NULL,
        megabytes REAL NOT NULL,
        network_type TEXT,
        description TEXT,
        recorded_at TEXT NOT NULL,
        created_at TEXT NOT NULL DEFAULT (datetime('now')),
        FOREIGN KEY(device_id) REFERENCES devices(id) ON DELETE CASCADE
      )`
    );
  });
}

const ensureSchema = async () => {
  if (!client || schemaInitialized) return;

  try {
    await client.execute(
      `CREATE TABLE IF NOT EXISTS devices (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        sim_number TEXT,
        data_limit_mb INTEGER,
        created_at TEXT NOT NULL DEFAULT (datetime('now'))
      )`
    );

    await client.execute(
      `CREATE TABLE IF NOT EXISTS usage_logs (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        device_id INTEGER NOT NULL,
        megabytes REAL NOT NULL,
        network_type TEXT,
        description TEXT,
        recorded_at TEXT NOT NULL,
        created_at TEXT NOT NULL DEFAULT (datetime('now')),
        FOREIGN KEY(device_id) REFERENCES devices(id) ON DELETE CASCADE
      )`
    );

    schemaInitialized = true;
  } catch (err) {
    console.error('Failed to ensure Turso schema on demand', err);
    throw err;
  }
};

const run = (sql, params = []) => {
  if (client) {
    const exec = () => client.execute({ sql, args: params });

    return exec()
      .catch(async (err) => {
        const message = String(err && err.message ? err.message : err);
        if (message.includes('no such table')) {
          await ensureSchema();
          return exec();
        }
        throw err;
      })
      .then((result) => {
        let insertedId = null;
        if (typeof result.lastInsertRowid === 'bigint') {
          insertedId = Number(result.lastInsertRowid);
        } else if (typeof result.lastInsertRowid === 'number') {
          insertedId = result.lastInsertRowid;
        }

        return {
          id: insertedId,
          changes: typeof result.rowsAffected === 'number' ? result.rowsAffected : 0,
        };
      });
  }

  return new Promise((resolve, reject) => {
    db.run(sql, params, function onComplete(err) {
      if (err) {
        reject(err);
      } else {
        resolve({ id: this.lastID, changes: this.changes });
      }
    });
  });
};

const get = (sql, params = []) => {
  if (client) {
    const exec = () => client.execute({ sql, args: params });

    return exec()
      .catch(async (err) => {
        const message = String(err && err.message ? err.message : err);
        if (message.includes('no such table')) {
          await ensureSchema();
          return exec();
        }
        throw err;
      })
      .then((result) => {
        const [row] = result.rows ?? [];
        return row ?? null;
      });
  }

  return new Promise((resolve, reject) => {
    db.get(sql, params, (err, row) => {
      if (err) {
        reject(err);
      } else {
        resolve(row);
      }
    });
  });
};

const all = (sql, params = []) => {
  if (client) {
    const exec = () => client.execute({ sql, args: params });

    return exec()
      .catch(async (err) => {
        const message = String(err && err.message ? err.message : err);
        if (message.includes('no such table')) {
          await ensureSchema();
          return exec();
        }
        throw err;
      })
      .then((result) => result.rows || []);
  }

  return new Promise((resolve, reject) => {
    db.all(sql, params, (err, rows) => {
      if (err) {
        reject(err);
      } else {
        resolve(rows);
      }
    });
  });
};

module.exports = {
  db,
  run,
  get,
  all,
};
