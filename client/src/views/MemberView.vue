<template>
  <div class="members">
    <!-- Spinner -->
    <div class="spinner-wrapper" v-if="isLoading">
      <loading-spinner id="spinner" :spin="isLoading" />
      <div class="loading-message">Loading members...</div>
    </div>

    <!-- Main content when not loading -->
    <div v-else>
      <!-- Nav tabs -->
      <nav class="sub-nav">
        <router-link :to="{ name: 'home' }" class="tab">LIFTS</router-link>
        <router-link :to="{ name: 'workouts' }" class="tab">WORKOUTS</router-link>
        <router-link :to="{ name: 'members' }" class="tab active">MEMBERS</router-link>
      </nav>

      <!-- Members table -->
      <table class="members-table">
        <thead>
          <tr>
            <th>FIRST NAME</th>
            <th>LAST NAME</th>
            <th>EMAIL</th>
            <th>BIRTH DATE</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in members" :key="m.personId">
            <td>{{ m.firstName }}</td>
            <td>{{ m.lastName }}</td>
            <td>{{ m.email }}</td>
            <td>{{ m.birthdate }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import ResourceService from "../services/ResourceService";
import LoadingSpinner from "../components/LoadingSpinner.vue";

export default {
  name: "MembersView",
  components: { LoadingSpinner },
  data() {
    return {
      members: [],
      isLoading: false,
    };
  },
  created() {
    this.isLoading = true;
    ResourceService.getMembers()
      .then((res) => {
        this.members = res.data;
      })
      .catch((err) => {
        console.error("Failed to load members:", err);
      })
      .finally(() => {
        this.isLoading = false;
      });
  },
};
</script>

<style scoped>
.members {
  padding: 0 2rem;
}

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
  margin: 2rem auto;
  max-width: 1000px;
}

.tab {
  position: relative;
  padding: 0.5rem 0;
  font-weight: 500;
  font-size: 0.85rem;
  color: #444;
  text-decoration: none;
  letter-spacing: 0.05em;
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

/* TABLE STYLING */
.members-table {
  width: 100%;
  border-collapse: collapse;
  margin: 0 auto 2rem auto;
  max-width: 1000px;
}

.members-table th {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #555;
  border-bottom: 1px solid #ddd;
  padding: 0.75rem 0.5rem;
  text-align: left;
}

.members-table td {
  font-size: 0.85rem;
  padding: 0.9rem 0.5rem;
  border-bottom: 1px solid #eee;
  vertical-align: top;
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
body.dark-mode .members-table th,
body.dark-mode .members-table td {
  color: #eee;
  border-color: #555;
}

@media (max-width: 425px) {
  .members {
    padding: 0 1rem;
    margin-top: 4rem;
  }

  .sub-nav {
    margin-top: 1rem;
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

  .members-table {
    display: block;
    overflow-x: auto;
    white-space: nowrap;
    margin-top: 1rem;
  }

  .members-table table {
    width: 100%;
    border-collapse: collapse;
  }

  .members-table th,
  .members-table td {
    font-size: 0.8rem;
    padding: 0.5rem;
  }
}
</style>