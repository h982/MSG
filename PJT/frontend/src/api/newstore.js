import { axiosService } from '@/api/index.js';

function addNewStore(store,callback, errorCallback) {
    axiosService
        .post('/newStore/add', store)
        .then((res) => {
            callback(res.data);
        })
        .catch((err) => {
            errorCallback(err);
        });
}

export { addNewStore };
