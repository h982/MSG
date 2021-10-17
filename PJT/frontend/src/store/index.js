import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import user from './user';
import header from './header';
import loading from './loading';
import page from './page';
import store from './store';
import analysis from './analysis';
import review from './review';

export default new Vuex.Store({
  modules: {
    user,
    header,
    loading,
    store,
    analysis,
    page,
    review,
  },
  plugins: [
    createPersistedState({
      paths: ['user', 'header', 'loading', 'store', 'analysis', 'page', 'review'],
    }),
  ],
});
