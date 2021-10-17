const state = {
    analysisType: 1,
  };
  
  const getters = {
    analysisType(state) {
      return state.analysisType;
    },
  };
  
  const mutations = {
    SET_ANALYSISTYPE(state, payload) {
      state.analysisType  = payload;
    },
  };
  
  const actions = {
    async set_analysisType(context, data) {
      await context.commit('SET_ANALYSISTYPE', data);
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
  