const state = {
  store: null,
  vsStore: null,
  words: [],
  vsWords: [],
};

const getters = {
  store(state) {
    return state.store;
  },
  vsStore(state) {
    return state.vsStore;
  },
  words(state) {
    return state.words;
  },
  vsWords(state) {
    return state.vsWords;
  },
};

const mutations = {
  SET_STORE(state, payload) {
    state.store = payload;
  },
  SET_VSSTORE(state, payload) {
    state.vsStore = payload;
  },
  SET_WORDS(state, payload) {
    state.words = payload;
  },
  SET_VSWORDS(state, payload) {
    state.vsWords = payload;
  },
};

const actions = {
  async set_store(context, data) {
    await context.commit("SET_STORE", data);
  },
  async set_vsStore(context, data) {
    await context.commit("SET_VSSTORE", data);
  },
  async set_words(context, data) {
    await context.commit("SET_WORDS", data);
  },
  async set_vsWords(context, data) {
    await context.commit("SET_VSWORDS", data);
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
