const state = {
  isOpen: false,
  type: '',
};

const getters = {
  isOpen(state) {
    return state.isOpen;
  },
  type(state) {
    return state.type;
  },
  // get_id: (state) => state.id,
};

const mutations = {
  TOGGLE_ISOPEN(state, payload) {
    state.isOpen = payload;
  },
  SET_TYPE(state, payload) {
    state.type = payload;
  },
};

const actions = {
  toggle_isOpen(context, data) {
    context.commit('TOGGLE_ISOPEN', data);
  },
  set_type(context, data) {
    context.commit('SET_TYPE', data);
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
