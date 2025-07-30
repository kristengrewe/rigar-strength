import { createRouter as createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

// Import components
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import LogoutView from '../views/LogoutView.vue'
import RegisterView from '../views/RegisterView.vue'
import CreateWorkoutView from '../views/CreateWorkoutView.vue'
import UpdateWorkoutView from "../views/UpdateWorkoutView.vue"
import WorkoutListView from '../views/WorkoutListView.vue'
import MemberView from '../views/MemberView.vue'


/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 */
const routes = [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: LogoutView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: RegisterView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/createworkout",
      name: "createworkout",
      component: CreateWorkoutView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/updateworkout/:workoutId",
      name: "updateworkout",
      component: UpdateWorkoutView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/workouts",
      name: "workouts",
      component: WorkoutListView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/members",
      name: "members",
      component: MemberView,
      meta: {
        requiresAuth: true
      }
    }
  ];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return {name: "login"};
  }
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;
