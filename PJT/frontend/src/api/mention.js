import { axiosService } from '@/api/index.js';

function getMention(keyword, callback, errorCallback) {
    
    axiosService
        .get('/naverAPI', { params: { keyword: keyword } })
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });

}

export { getMention };
