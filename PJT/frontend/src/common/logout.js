import { logoutUser } from '@/api/auth.js';

function logout(id) {
  logoutUser(
    id,
    (res) => {
      if (res.data) {
        const CLIENT_ID = process.env.VUE_APP_KAKAO_ID;
        const REDIRECT_URI = process.env.VUE_APP_LOGOUT_KAKAO_URI;

        window.location.replace(
          'https://kauth.kakao.com/oauth/authorize?client_id=' +
            CLIENT_ID +
            '&redirect_uri=' +
            REDIRECT_URI +
            '&response_type=code'
        );
      }
    },
    (error) => {
      alert('문제가 발생했습니다. 다시 시도해주세요.');
      console.log(error);
    }
  );
}

export { logout };
