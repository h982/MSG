import axios from 'axios';
import { setInterceptors } from './common/interceptors';

function createAxiosService() {
  return axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    headers: {
      'Content-type': 'application/json',
    },
  });
}

function createAxiosServiceWithAuth() {
  const axiosService = axios.create({
    baseURL: `http://localhost:8080`,
    withCredentials: true,
    headers: {
      'Content-type': 'application/json',
    },
  });

  return setInterceptors(axiosService);
}

export const axiosService = createAxiosService();
export const axiosServiceWithAuth = createAxiosServiceWithAuth();
