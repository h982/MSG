<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <div class="flex flex-col justify-center items-center flex-grow bg-blue-100 w-auto pt-10 pb-10">
      <div class="h-20 mb-12 mx-auto">
        <img class="h-full w-auto" src="@/images/banner.png" />
      </div>
      <img class="hover: cursor-pointer" @click="kakaoLogin" src="@/images/kakaoLogin.png" />
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { loginUser } from '@/api/auth.js';
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'Login',
  components: { Header, Footer },
  data: () => {
    return {
      code: '',
    };
  },
  created() {
    this.code = this.$route.query.code;
  },
  mounted() {
    if (this.code) {
      this.login();
    }
  },
  methods: {
    ...mapActions(['toggle_isLogin', 'set_id', 'set_nickname', 'set_type', 'toggle_isLoading']),
    kakaoLogin() {
      this.toggle_isLoading(true);
      this.set_type('login');

      const CLIENT_ID = process.env.VUE_APP_KAKAO_ID;
      const REDIRECT_URI = process.env.VUE_APP_LOGIN_KAKAO_URI;

      window.location.replace(
        'https://kauth.kakao.com/oauth/authorize?client_id=' +
          CLIENT_ID +
          '&redirect_uri=' +
          REDIRECT_URI +
          '&response_type=code'
      );
    },
    login() {
      setTimeout(() => {
        loginUser(
          this.code,
          (res) => {
            if (res.object.member) {
              this.set_id(res.object.mid);
              this.set_nickname(res.object.nickname);
              this.toggle_isLogin(true);

              this.$router.push('Main');
            } else {
              alert('회원가입이 필요합니다. \n회원가입 페이지로 이동합니다.');
              this.$router.push('Signup');
            }
          },
          (error) => {
            alert('문제가 발생했습니다. 다시 시도해주세요.');
            console.log(error);
            this.$router.push('Main');
          }
        );
      }, 1000);
    },
  },
  computed: {
    ...mapGetters(['isLoading']),
  },
};
</script>
