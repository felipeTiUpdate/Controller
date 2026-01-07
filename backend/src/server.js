const express = require('express');
const cors = require('cors');

const devicesRouter = require('./routes/devices');
const reportsRouter = require('./routes/reports');

const app = express();
const PORT = process.env.PORT || 4000;

app.use(cors());
app.use(express.json());

app.get('/health', (_req, res) => {
  res.json({ status: 'ok' });
});

app.use('/devices', devicesRouter);
app.use('/reports', reportsRouter);

app.use((err, _req, res, _next) => {
  console.error(err);
  res.status(err.status || 500).json({ error: err.message || 'Internal server error' });
});

app.listen(PORT, () => {
  console.log(`Mobile data monitor backend running on port ${PORT}`);
});
