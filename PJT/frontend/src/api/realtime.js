import { axiosService } from '@/api/index.js';

function getRealtime(callback, errorCallback) {
    
    axiosService
        .get('/realtime')
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });
}

export { getRealtime };
