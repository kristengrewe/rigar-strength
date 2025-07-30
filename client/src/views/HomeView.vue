<template>
  <div class="home">
    <!-- Spinner -->
    <div class="spinner-wrapper" v-if="isLoading">
      <loading-spinner id="spinner" :spin="isLoading" />
      <div class="loading-message">Loading lifts...</div>
    </div>

    <!-- Main content -->
    <div v-else>
      <!-- SUB NAV -->
      <nav class="sub-nav">
        <router-link
          :to="{ name: 'home' }"
          class="tab"
          :class="{ active: $route.name === 'home' }"
        >
          LIFTS
        </router-link>

        <router-link
          :to="{ name: 'workouts' }"
          class="tab"
          :class="{ active: $route.name === 'workouts' }"
        >
          WORKOUTS
        </router-link>

        <router-link
          v-if="isAdmin"
          :to="{ name: 'members' }"
          class="tab"
          :class="{ active: $route.name === 'members' }"
        >
          MEMBERS
        </router-link>
      </nav>

      <!-- Lifts grid -->
      <p v-if="lifts.length === 0">No lifts found.</p>
      <div v-else class="lifts-grid">
        <div v-for="lift in lifts" :key="lift.liftId" class="lift-box">
          {{ lift.liftName }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LoadingSpinner from "../components/LoadingSpinner.vue";
import ResourceService from "../services/ResourceService";

export default {
  name: "HomeView",
  components: { LoadingSpinner },
  data() {
    return {
      isLoading: false,
      lifts: []
    };
  },
  computed: {
    isAdmin() {
      const user = this.$store.state.user;
      return user && user.role === "ROLE_ADMIN";
    }
  },
  created() {
    this.isLoading = true;
    ResourceService.getLifts()
      .then((response) => {
        this.lifts = response.data;
      })
      .catch((error) => {
        console.error("Error loading lifts:", error);
      })
      .finally(() => {
        this.isLoading = false;
      });
  }
};
</script>

<style scoped>
.spinner-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1rem 0;
}

#spinner {
  font-size: 1.5rem;
}

.loading-message {
  margin-top: 0.5rem;
  font-style: italic;
  text-align: center;
}

.sub-nav {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin: 2rem auto 1rem auto;
  padding: 0 2rem;
  max-width: 800px;
}

.tab {
  position: relative;
  padding: 0.5rem 0;
  font-weight: 500;
  font-size: 0.9rem;
  color: #444;
  text-decoration: none;
  transition: color 0.2s ease;
}
.tab:hover {
  color: #000;
}
.tab.active {
  font-weight: bold;
  color: #000;
}
.tab.active::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -1px;
  width: 100%;
  height: 2px;
  background-color: #000;
}

/* DARK MODE */
body.dark-mode .tab {
  color: #ddd;
}
body.dark-mode .tab:hover {
  color: #fff;
}
body.dark-mode .tab.active {
  color: #fff;
}
body.dark-mode .tab.active::after {
  background-color: #fff;
}

.lifts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-top: 1rem;
  padding: 0 2rem;
  padding-bottom: 3rem;
}

.lift-box {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
  font-weight: bold;
  background-color: #f9f9f9;
  color: #000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

body.dark-mode .lift-box {
  background-color: #222;
  color: #eee;
  border-color: #444;
}

@media (max-width: 425px) {
  .sub-nav {
    margin-top: 5rem;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
    padding: 0 0.5rem;
  }

  .tab {
    font-size: 0.85rem;
    padding: 0.4rem 0;
    white-space: nowrap;
  }

  .lifts-grid {
    grid-template-columns: 1fr;
    gap: 0.75rem;
    padding: 0 1rem;
  }

  .lift-box {
    font-size: 0.95rem;
    padding: 0.75rem;
  }
}
</style>
