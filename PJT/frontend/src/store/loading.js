const state = {
  isLoading: false,
};

const getters = {
  isLoading(state) {
    return state.isLoading;
  },
  // get_id: (state) => state.id,
};

const mutations = {
  TOGGLE_ISLOADING(state, payload) {
    state.isLoading = payload;
  },
};

const actions = {
  async toggle_isLoading(context, data) {
    await context.commit('TOGGLE_ISLOADING', data);
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
