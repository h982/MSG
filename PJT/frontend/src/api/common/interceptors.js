import axios from 'axios';
import router from '@/router';
import store from '@/store/index';
import { logout } from '@/common/logout.js';
import { reissuUser } from '@/api/auth.js';

export function setInterceptors(axiosService) {
  axiosService.interceptors.request.use(
    function (config) {
      // 요청을 보내기 전에 어떤 처리를 할 수 있다.
      return config;
    },
    function (error) {
      // 요청이 잘못되었을 때 에러가 컴포넌트 단으로 오기 전에 어떤 처리를 할 수 있다.
      return Promise.reject(error);
    }
  );

  let isTokenRefreshing = false;
  let refreshSubscribers = [];

  // 새로운 토큰이 발행되면 refreshSubscribers에 쌓인 요청들을 처리
  const onTokenRefreshed = () => {
    refreshSubscribers.map((callback) => callback());
  };

  // 새로운 토큰이 발행되기전에 refreshSubscribers에 요청을 쌓는다.
  const addRefreshSubscriber = (callback) => {
    refreshSubscribers.push(callback);
  };

  axiosService.interceptors.response.use(
    function (response) {
      // 서버에 요청을 보내고 나서 응답을 받기 전에 어떤 처리를 할 수 있다.
      return response;
    },
    async function (error) {
      const errorApi = error.config;

      if (error.response.data.status === 401) {
        if (!isTokenRefreshing) {
          errorApi.retry = true;

          // 재발급 요청
          reissuUser(
            store.getters.id,
            (res) => {
              // 재발급 요청에 성공할 경우
              if (res.object) {
                // console.log('토큰 재발급 성공');
                return axios(errorApi);
              } else {
                // 재발급 요청에 실패했을 경우
                alert('로그인 유효기간이 만료되었습니다. \n다시 로그인해주세요.');
                store.dispatch('set_type', 'logout');
                store.dispatch('toggle_isLoading', true);
                logout(store.getters.id);
              }
            },
            (error) => {
              alert('문제가 발생했습니다. 다시 시도해주세요.');
              console.log(error);
              router.push('Main');
            }
          );

          // const { data } = await axiosService.post('/member/reissue', userData).catch((err) => {
          //   console.log(err);
          //   alert("문제가 발생")
          // });

          isTokenRefreshing = false;
          // 새로운 토큰으로 지연된 요청 진행
          onTokenRefreshed();
        }
        // 토큰이 재발급 되는 동안의 요청은 refreshSubscribers에 저장
        const retryOriginalRequest = new Promise((resolve) => {
          addRefreshSubscriber(() => {
            resolve(axios(errorApi));
          });
        });

        return retryOriginalRequest;
      } else {
        alert('문제가 발생했습니다. 다시 시도해주세요.');
        console.log(error);
        router.push('Main');
      }

      // 응답이 에러인 경우에 미리 전처리할 수 있다.
      return Promise.reject(error);
    }
  );

  return axiosService;
}
