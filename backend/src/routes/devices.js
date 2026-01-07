const express = require('express');
const dayjs = require('dayjs');
const { run, all, get } = require('../db');

const router = express.Router();

router.get('/', async (_req, res, next) => {
  try {
    const devices = await all(
      `SELECT d.*, 
              IFNULL(SUM(u.megabytes), 0) AS total_usage
       FROM devices d
       LEFT JOIN usage_logs u ON u.device_id = d.id
       GROUP BY d.id
       ORDER BY d.created_at DESC`
    );

    res.json(
      devices.map((device) => ({
        id: device.id,
        name: device.name,
        simNumber: device.sim_number,
        dataLimitMb: device.data_limit_mb,
        createdAt: device.created_at,
        usage: {
          totalMb: Number(device.total_usage || 0),
          percentage: device.data_limit_mb
            ? Number(((device.total_usage || 0) / device.data_limit_mb) * 100).toFixed(2)
            : null,
        },
      }))
    );
  } catch (error) {
    next(error);
  }
});

router.post('/', async (req, res, next) => {
  try {
    const { name, simNumber, dataLimitMb } = req.body;

    if (!name || typeof name !== 'string') {
      return res.status(400).json({ error: 'name is required' });
    }

    const normalizedLimit =
      dataLimitMb === undefined || dataLimitMb === null
        ? null
        : Number.isFinite(Number(dataLimitMb))
        ? Number(dataLimitMb)
        : NaN;

    if (Number.isNaN(normalizedLimit)) {
      return res.status(400).json({ error: 'dataLimitMb must be a number' });
    }

    const result = await run(
      `INSERT INTO devices (name, sim_number, data_limit_mb)
       VALUES (?, ?, ?)`
        .replace(/\n\s+/g, ' '),
      [name.trim(), simNumber || null, normalizedLimit]
    );

    const device = await get(`SELECT * FROM devices WHERE id = ?`, [result.id]);

    res.status(201).json({
      id: device.id,
      name: device.name,
      simNumber: device.sim_number,
      dataLimitMb: device.data_limit_mb,
      createdAt: device.created_at,
    });
  } catch (error) {
    next(error);
  }
});

router.put('/:id', async (req, res, next) => {
  try {
    const { id } = req.params;
    const { name, simNumber, dataLimitMb } = req.body;

    const existing = await get(`SELECT * FROM devices WHERE id = ?`, [id]);
    if (!existing) {
      return res.status(404).json({ error: 'Device not found' });
    }

    const updatedName = typeof name === 'string' && name.trim() ? name.trim() : existing.name;
    const updatedSim = simNumber !== undefined ? simNumber : existing.sim_number;
    const updatedLimit =
      dataLimitMb === null
        ? null
        : dataLimitMb !== undefined
        ? Number(dataLimitMb)
        : existing.data_limit_mb;

    if (updatedLimit !== null && !Number.isFinite(updatedLimit)) {
      return res.status(400).json({ error: 'dataLimitMb must be a number or null' });
    }

    await run(
      `UPDATE devices
       SET name = ?, sim_number = ?, data_limit_mb = ?
       WHERE id = ?`
        .replace(/\n\s+/g, ' '),
      [updatedName, updatedSim, updatedLimit, id]
    );

    const device = await get(`SELECT * FROM devices WHERE id = ?`, [id]);

    res.json({
      id: device.id,
      name: device.name,
      simNumber: device.sim_number,
      dataLimitMb: device.data_limit_mb,
      createdAt: device.created_at,
    });
  } catch (error) {
    next(error);
  }
});

router.delete('/:id', async (req, res, next) => {
  try {
    const { id } = req.params;
    const result = await run(`DELETE FROM devices WHERE id = ?`, [id]);
    if (!result.changes) {
      return res.status(404).json({ error: 'Device not found' });
    }

    res.status(204).send();
  } catch (error) {
    next(error);
  }
});

router.post('/:id/usage', async (req, res, next) => {
  try {
    const { id } = req.params;
    const { megabytes, networkType, description, recordedAt } = req.body;

    const device = await get(`SELECT id FROM devices WHERE id = ?`, [id]);
    if (!device) {
      return res.status(404).json({ error: 'Device not found' });
    }

    const amount = Number(megabytes);
    if (!Number.isFinite(amount) || amount <= 0) {
      return res.status(400).json({ error: 'megabytes must be a positive number' });
    }

    const timestamp = recordedAt ? dayjs(recordedAt) : dayjs();
    if (!timestamp.isValid()) {
      return res.status(400).json({ error: 'recordedAt must be a valid date' });
    }

    const normalizedNetwork = networkType ? String(networkType).toUpperCase() : null;

    const result = await run(
      `INSERT INTO usage_logs (device_id, megabytes, network_type, description, recorded_at)
       VALUES (?, ?, ?, ?, ?)`
        .replace(/\n\s+/g, ' '),
      [id, amount, normalizedNetwork, description || null, timestamp.toISOString()]
    );

    const usage = await get(`SELECT * FROM usage_logs WHERE id = ?`, [result.id]);

    res.status(201).json({
      id: usage.id,
      deviceId: usage.device_id,
      megabytes: usage.megabytes,
      networkType: usage.network_type,
      description: usage.description,
      recordedAt: usage.recorded_at,
      createdAt: usage.created_at,
    });
  } catch (error) {
    next(error);
  }
});

router.get('/:id/usage', async (req, res, next) => {
  try {
    const { id } = req.params;
    const { startDate, endDate } = req.query;

    const device = await get(`SELECT * FROM devices WHERE id = ?`, [id]);
    if (!device) {
      return res.status(404).json({ error: 'Device not found' });
    }

    let start = startDate ? dayjs(startDate).startOf('day') : null;
    let end = endDate ? dayjs(endDate).endOf('day') : null;

    if (startDate && (!start || !start.isValid())) {
      return res.status(400).json({ error: 'startDate is invalid' });
    }

    if (endDate && (!end || !end.isValid())) {
      return res.status(400).json({ error: 'endDate is invalid' });
    }

    if (start && end && end.isBefore(start)) {
      return res.status(400).json({ error: 'endDate must be after startDate' });
    }

    const conditions = ['device_id = ?'];
    const params = [id];

    if (start) {
      conditions.push('recorded_at >= ?');
      params.push(start.toISOString());
    }
    if (end) {
      conditions.push('recorded_at <= ?');
      params.push(end.toISOString());
    }

    const usageEntries = await all(
      `SELECT *
       FROM usage_logs
       WHERE ${conditions.join(' AND ')}
       ORDER BY recorded_at DESC`
        .replace(/\n\s+/g, ' '),
      params
    );

    const total = usageEntries.reduce((sum, item) => sum + Number(item.megabytes), 0);

    res.json({
      device: {
        id: device.id,
        name: device.name,
        simNumber: device.sim_number,
        dataLimitMb: device.data_limit_mb,
      },
      filters: {
        startDate: start ? start.toISOString() : null,
        endDate: end ? end.toISOString() : null,
      },
      usage: {
        totalMb: total,
        entries: usageEntries.map((entry) => ({
          id: entry.id,
          megabytes: entry.megabytes,
          networkType: entry.network_type,
          description: entry.description,
          recordedAt: entry.recorded_at,
          createdAt: entry.created_at,
        })),
      },
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
