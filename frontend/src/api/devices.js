import api from './client.js';

export const listDevices = () => api.get('/devices').then((res) => res.data);

export const getDeviceUsage = (deviceId, params = {}) =>
  api.get(`/devices/${deviceId}/usage`, { params }).then((res) => res.data);

export const createDevice = (payload) => api.post('/devices', payload).then((res) => res.data);

export const updateDevice = (deviceId, payload) =>
  api.put(`/devices/${deviceId}`, payload).then((res) => res.data);

export const deleteDevice = (deviceId) => api.delete(`/devices/${deviceId}`).then((res) => res.data);
