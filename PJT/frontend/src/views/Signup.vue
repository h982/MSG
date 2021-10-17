<template>
  <div class="flex flex-col h-screen w-auto">
    <Header />
    <div class="flex flex-col justify-center items-center flex-grow bg-blue-100 w-auto pt-10 pb-10">
      <div class="h-20 mb-12 mx-auto">
        <img class="h-full w-auto" src="@/images/banner.png" />
      </div>

      <div class="flex flex-col items-center justify-center">
        <div>
          <input
            class="
              appearance-none
              border border-transparent
              w-72
              py-2
              px-4
              bg-white
              text-gray-700
              placeholder-gray-400
              shadow-md
              rounded-lg
              text-base
              focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-transparent
            "
            placeholder="닉네임을 입력하세요"
            name="nickname"
            v-model="nickname"
          />
          <button
            @click="nicknameDupChk"
            class="
              ml-2
              focus-within:disabled:opacity-50
              flex-shrink-0
              bg-purple-600
              text-white text-base
              font-semibold
              py-2
              px-4
              rounded-lg
              shadow-md
              hover:bg-purple-700
              focus:outline-none
              focus:ring-2
              focus:ring-purple-500
              focus:ring-offset-2
              focus:ring-offset-purple-200
              disabled:opacity-50
            "
            :disabled="!meta.valid"
          >
            중복검사
          </button>
        </div>
        <span class="text-red-400 font-bold my-5">{{ nicknameError }}</span>
        <button
          @click="goKakaoLogin"
          class="disabled:opacity-50 bg-blue-100"
          :disabled="!meta.valid || !nicknameChk"
        >
          <img class="hover: cursor-pointer h-full w-full" src="@/images/kakaoSignup.png" />
        </button>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';
import { nicknameCheck, signUpUser } from '@/api/auth.js';
import { useForm, useField } from 'vee-validate';
import { mapActions, mapGetters } from 'vuex';
import * as Yup from 'yup';

export default {
  name: 'SIGNUP',
  components: {
    Header,
    Footer,
  },
  data: () => {
    return {
      code: '',
      nicknameChk: false,
    };
  },
  setup() {
    const schema = Yup.object({
      nickname: Yup.string()
        .min(2, '닉네임은 최소 2글자 이상 작성해야합니다.')
        .max(8, '닉네임은 최대 8글자 입니다.')
        .matches(/^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]+$/, '닉네임은 숫자, 영어, 한글로만 작성해야합니다.')
        .required('닉네임을 입력해주세요.'),
    });

    useForm({
      validationSchema: schema,
    });

    const { value: nickname, errorMessage: nicknameError, meta } = useField('nickname');

    return {
      nickname,
      nicknameError,
      meta,
    };
  },
  created() {
    this.code = this.$route.query.code;
  },
  mounted() {
    if (this.code) {
      this.toggle_isLoading(true);
      this.signup();
    }
  },
  methods: {
    ...mapActions(['set_signupnickname', 'toggle_isLoading']),
    kakaoLogin() {
      const CLIENT_ID = process.env.VUE_APP_KAKAO_ID;
      const REDIRECT_URI = process.env.VUE_APP_SIGNUP_KAKAO_URI;

      window.location.replace(
        'https://kauth.kakao.com/oauth/authorize?client_id=' +
          CLIENT_ID +
          '&redirect_uri=' +
          REDIRECT_URI +
          '&response_type=code'
      );
    },
    signup() {
      const userData = {
        authorizeCode: this.code,
        nickname: this.signupNickname,
      };
      signUpUser(
        userData,
        (res) => {
          if (res.object) {
            alert('회원가입 완료! \n로그인 페이지로 이동합니다.');
          } else {
            alert('이미 가입된 회원입니다. \n로그인 페이지로 이동합니다.');
          }

          this.toggle_isLoading(false);
          this.$router.push({ name: 'Login' });
        },
        (error) => {
          console.log(error);
          alert('문제가 발생했습니다. 다시 시도해주세요.');
        }
      );
    },
    async goKakaoLogin() {
      await this.set_signupnickname(this.nickname);
      this.kakaoLogin();
    },
    nicknameDupChk() {
      nicknameCheck(
        this.nickname,
        (res) => {
          this.nicknameChk = res.object;

          if (this.nicknameChk) {
            alert('중복된 닉네임이 없습니다. \n회원가입을 진행해주세요.');
          } else {
            alert('중복된 닉네임이 있습니다.');
          }
        },
        (error) => {
          alert('문제가 발생했습니다. 다시 시도해주세요.');
          console.log(error);
          // this.$router.push('/');
        }
      );
    },
  },
  computed: {
    ...mapGetters(['signupNickname']),
  },
};
</script>
