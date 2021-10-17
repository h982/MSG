const state = {
  reviewValidation: false,
};

const getters = {
  reviewValidation(state) {
    return state.reviewValidation;
  },
};

const mutations = {
  SET_REVIEWVALIDATION(state, payload) {
    state.reviewValidation = payload;
  },
};

const actions = {
  async set_reviewValidation(context, data) {
    await context.commit("SET_REVIEWVALIDATION", data);
  },
};

export default {
  strict: process.env.NODE_ENV !== "production",
  state: {
    ...state,
  },
  getters,
  mutations,
  actions,
};