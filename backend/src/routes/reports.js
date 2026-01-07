const express = require('express');
const dayjs = require('dayjs');
const isoWeek = require('dayjs/plugin/isoWeek');

dayjs.extend(isoWeek);
const { all } = require('../db');

const router = express.Router();

const parseRange = (startDate, endDate) => {
  const start = startDate ? dayjs(startDate).startOf('day') : dayjs().startOf('month');
  const end = endDate ? dayjs(endDate).endOf('day') : dayjs().endOf('day');

  if (!start.isValid() || !end.isValid()) {
    const error = new Error('Invalid date range');
    error.status = 400;
    throw error;
  }

  if (end.isBefore(start)) {
    const error = new Error('endDate must be after startDate');
    error.status = 400;
    throw error;
  }

  return { start, end };
};

router.get('/overview', async (req, res, next) => {
  try {
    const { startDate, endDate } = req.query;
    const { start, end } = parseRange(startDate, endDate);

    const rows = await all(
      `SELECT d.id,
              d.name,
              d.sim_number,
              d.data_limit_mb,
              IFNULL(SUM(u.megabytes), 0) AS total_usage
       FROM devices d
       LEFT JOIN usage_logs u
         ON u.device_id = d.id AND u.recorded_at BETWEEN ? AND ?
       GROUP BY d.id
       ORDER BY d.name ASC`,
      [start.toISOString(), end.toISOString()]
    );

    const devices = rows.map((row) => ({
      id: row.id,
      name: row.name,
      simNumber: row.sim_number,
      dataLimitMb: row.data_limit_mb,
      usage: {
        totalMb: Number(row.total_usage || 0),
        percentage: row.data_limit_mb
          ? Number(((row.total_usage || 0) / row.data_limit_mb) * 100).toFixed(2)
          : null,
      },
    }));

    const totalUsage = devices.reduce((sum, item) => sum + item.usage.totalMb, 0);
    const devicesWithLimit = devices.filter((device) => Number.isFinite(device.dataLimitMb));

    res.json({
      range: {
        start: start.toISOString(),
        end: end.toISOString(),
      },
      totals: {
        devices: devices.length,
        usageMb: totalUsage,
        averageUsagePerDeviceMb: devices.length ? totalUsage / devices.length : 0,
        averageLimitMb: devicesWithLimit.length
          ? devicesWithLimit.reduce((sum, device) => sum + device.dataLimitMb, 0) /
            devicesWithLimit.length
          : null,
      },
      devices,
    });
  } catch (error) {
    next(error);
  }
});

router.get('/trends', async (req, res, next) => {
  try {
    const { startDate, endDate, granularity = 'day' } = req.query;
    const { start, end } = parseRange(startDate, endDate);

    const unit = ['day', 'week', 'month'].includes(granularity) ? granularity : 'day';

    const rows = await all(
      `SELECT DATE(strftime('%Y-%m-%d', recorded_at)) AS day,
              SUM(megabytes) AS total_mb
       FROM usage_logs
       WHERE recorded_at BETWEEN ? AND ?
       GROUP BY day
       ORDER BY day ASC`,
      [start.toISOString(), end.toISOString()]
    );

    const buckets = {};
    rows.forEach((row) => {
      const date = dayjs(row.day);
      let key;
      if (unit === 'day') {
        key = date.format('YYYY-MM-DD');
      } else if (unit === 'week') {
        key = `${date.isoWeekYear()}-W${date.isoWeek()}`;
      } else {
        key = date.format('YYYY-MM');
      }

      if (!buckets[key]) {
        buckets[key] = 0;
      }
      buckets[key] += Number(row.total_mb || 0);
    });

    const timeline = Object.entries(buckets).map(([bucket, megabytes]) => ({ bucket, megabytes }));

    res.json({
      range: {
        start: start.toISOString(),
        end: end.toISOString(),
      },
      granularity: unit,
      timeline,
    });
  } catch (error) {
    next(error);
  }
});

router.get('/network', async (req, res, next) => {
  try {
    const { startDate, endDate } = req.query;
    const { start, end } = parseRange(startDate, endDate);

    const rows = await all(
      `SELECT IFNULL(network_type, 'UNKNOWN') AS network,
              SUM(megabytes) AS total_mb,
              COUNT(*) AS entries
       FROM usage_logs
       WHERE recorded_at BETWEEN ? AND ?
       GROUP BY network
       ORDER BY total_mb DESC`,
      [start.toISOString(), end.toISOString()]
    );

    const totalMb = rows.reduce((sum, row) => sum + Number(row.total_mb || 0), 0);

    res.json({
      range: {
        start: start.toISOString(),
        end: end.toISOString(),
      },
      totalMb,
      breakdown: rows.map((row) => ({
        networkType: row.network,
        megabytes: Number(row.total_mb || 0),
        entries: row.entries,
        percentage: totalMb ? Number((Number(row.total_mb || 0) / totalMb) * 100).toFixed(2) : null,
      })),
    });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
