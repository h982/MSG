const state = {
  isLogin: false,
  signupNickname: '',
  id: '',
  nickname: '',
};

const getters = {
  isLogin(state) {
    return state.isLogin;
  },
  signupNickname(state) {
    return state.signupNickname;
  },
  id(state) {
    return state.id;
  },
  nickname(state) {
    return state.nickname;
  },
  // get_id: (state) => state.id,
};

const mutations = {
  TOGGLE_ISLOGIN(state, payload) {
    state.isLogin = payload;
  },
  SET_SIGNUPNICKNAME(state, payload) {
    state.signupNickname = payload;
  },
  SET_ID(state, payload) {
    state.id = payload;
  },
  SET_NICKNAME(state, payload) {
    state.nickname = payload;
  },
};

const actions = {
  async toggle_isLogin(context, data) {
    await context.commit('TOGGLE_ISLOGIN', data);
  },
  async toggle_isLoginToSideBar(context, data) {
    setTimeout(async function () {
      await context.commit('TOGGLE_ISLOGIN', data);
    }, 350);
  },
  async set_signupnickname(context, data) {
    await context.commit('SET_SIGNUPNICKNAME', data);
  },
  async set_id(context, data) {
    await context.commit('SET_ID', data);
  },
  async set_nickname(context, data) {
    await context.commit('SET_NICKNAME', data);
  },
};

export default {
  strict: process.env.NODE_ENV !== 'production',
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};
