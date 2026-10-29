import axios from 'axios';

const client = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080'
});

client.interceptors.request.use((config) => {
  const token = localStorage.getItem('talentsync-token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getApiErrorMessage = (error, fallback = 'Something went wrong. Please try again.') => {
  const payload = error?.response?.data;
  if (typeof payload?.message === 'string' && payload.message.trim()) return payload.message;
  if (typeof payload?.error === 'string' && payload.error.trim()) return payload.error;
  return fallback;
};

export default client;
