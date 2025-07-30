<template>
  <div class="workouts">
    <!-- Spinner or loading message -->
    <div class="spinner-wrapper" v-if="isLoading">
      <loading-spinner id="spinner" :spin="isLoading" />
      <div class="loading-message">Loading workouts...</div>
    </div>

    <div v-else>
      <!-- SUB NAV -->
      <nav class="sub-nav">
        <router-link :to="{ name: 'home' }" class="tab" :class="{ active: $route.name === 'home' }">
          LIFTS
        </router-link>
        <router-link :to="{ name: 'workouts' }" class="tab" :class="{ active: $route.name === 'workouts' }">
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

      <!-- FILTER BAR -->
      <div class="filter-bar">
        <div class="filter-dates">
          <div class="filter-date">
            <label for="start-date">Start Date</label>
            <input id="start-date" type="date" v-model="dateStart" />
          </div>
          <div class="filter-date">
            <label for="end-date">End Date</label>
            <input id="end-date" type="date" v-model="dateEnd" />
          </div>
        </div>

        <select v-model="selectedLift">
          <option value="">Lift Type</option>
          <option v-for="l in uniqueLifts" :key="l" :value="l">{{ l }}</option>
        </select>

        <select v-if="isAdmin" v-model="selectedMember">
          <option value="">Member Last Name</option>
          <option v-for="m in uniqueMembers" :key="m" :value="m">{{ m }}</option>
        </select>

        <router-link class="log-workout-btn" :to="{ name: 'createworkout' }">
          ⊕ LOG WORKOUT
        </router-link>
      </div>

      <!-- WORKOUTS TABLE -->
      <table class="workouts-table">
        <thead>
          <tr>
            <th @click="sortBy('date')">DATE <span>{{ sortIndicator('date') }}</span></th>
            <th v-if="isAdmin" @click="sortBy('personFirstName')">NAME <span>{{ sortIndicator('personFirstName') }}</span></th>
            <th @click="sortBy('liftName')">LIFT <span>{{ sortIndicator('liftName') }}</span></th>
            <th @click="sortBy('sets')">SETS <span>{{ sortIndicator('sets') }}</span></th>
            <th @click="sortBy('reps')">REPS <span>{{ sortIndicator('reps') }}</span></th>
            <th @click="sortBy('maxWeight')">MAX WEIGHT <span>{{ sortIndicator('maxWeight') }}</span></th>
            <th>NOTES</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="w in sortedFilteredWorkouts" :key="w.workoutId">
            <td>{{ formatDate(w.date) }}</td>
            <td v-if="isAdmin">{{ w.personFirstName }} {{ w.personLastName }}</td>
            <td>{{ w.liftName }}</td>
            <td>{{ w.sets }}</td>
            <td>{{ w.reps }}</td>
            <td>{{ w.maxWeight }}</td>
            <td>{{ w.notes || '' }}</td>
            <td class="actions">
              <template v-if="w.personId === userId">
                <router-link
                  :to="{ name: 'updateworkout', params: { workoutId: w.workoutId } }"
                  class="icon-btn"
                  title="Edit workout"
                >✎</router-link>
                <button @click="confirmDelete(w.workoutId)" class="icon-btn" title="Delete workout">🗑️</button>
              </template>
            </td>
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
  name: "WorkoutsView",
  components: { LoadingSpinner },
  data() {
    return {
      isLoading: false,
      workouts: [],
      dateStart: "",
      dateEnd: "",
      selectedLift: "",
      selectedMember: "",
      sortKey: "date",
      sortAsc: false
    };
  },
  computed: {
    isAdmin() {
      const user = this.$store.state.user;
      return user && user.role === "ROLE_ADMIN";
    },
    userId() {
      const user = this.$store.state.user;
      return user ? user.id : null;
    },
    uniqueLifts() {
      return [...new Set(this.workouts.map(w => w.liftName))];
    },
    uniqueMembers() {
      return [...new Set(this.workouts.map(w => w.personLastName))];
    },
    filteredWorkouts() {
      return this.workouts.filter(w => {
        const dateStr = this.formatDateISO(w.date);
        const matchesLift = this.selectedLift ? w.liftName === this.selectedLift : true;
        const matchesMember = this.isAdmin && this.selectedMember ? w.personLastName === this.selectedMember : true;
        if (this.dateStart && !this.dateEnd) {
          return dateStr === this.dateStart && matchesLift && matchesMember;
        }
        const matchesStart = this.dateStart ? dateStr >= this.dateStart : true;
        const matchesEnd = this.dateEnd ? dateStr <= this.dateEnd : true;
        return matchesStart && matchesEnd && matchesLift && matchesMember;
      });
    },
    sortedFilteredWorkouts() {
      return [...this.filteredWorkouts].sort((a, b) => {
        let aVal = a[this.sortKey];
        let bVal = b[this.sortKey];
        if (this.sortKey === "date") {
          aVal = new Date(aVal);
          bVal = new Date(bVal);
        } else if (this.sortKey === "personFirstName") {
          aVal = (a.personFirstName || "").toLowerCase();
          bVal = (b.personFirstName || "").toLowerCase();
        }
        if (aVal < bVal) return this.sortAsc ? -1 : 1;
        if (aVal > bVal) return this.sortAsc ? 1 : -1;
        return 0;
      });
    }
  },
  created() {
    this.loadWorkouts();
  },
  methods: {
    loadWorkouts() {
      this.isLoading = true;
      ResourceService.getWorkouts()
        .then(res => {
          this.workouts = res.data;
        })
        .catch(err => console.error("Failed to load workouts", err))
        .finally(() => {
          this.isLoading = false;
        });
    },
    confirmDelete(id) {
      if (confirm("Are you sure you want to delete this workout?")) {
        this.isLoading = true;
        ResourceService.deleteWorkout(id)
          .then(() => this.loadWorkouts())
          .catch(err => {
            console.error("Delete failed:", err);
            alert("There was an issue deleting the workout.");
          })
          .finally(() => {
            this.isLoading = false;
          });
      }
    },
    formatDate(dateStr) {
      const date = new Date(dateStr);
      return `${date.getMonth() + 1}/${date.getDate()}/${date
        .getFullYear()
        .toString()
        .slice(-2)}`;
    },
    formatDateISO(dateStr) {
      const date = new Date(dateStr);
      return date.toISOString().split("T")[0];
    },
    sortBy(key) {
      if (this.sortKey === key) {
        this.sortAsc = !this.sortAsc;
      } else {
        this.sortKey = key;
        this.sortAsc = true;
      }
    },
    sortIndicator(key) {
      if (this.sortKey !== key) return "";
      return this.sortAsc ? "▲" : "▼";
    }
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

/* --- keep your original styles below --- */
.sub-nav {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin: 2rem auto 1rem auto;
  padding: 0 2rem;
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

/* Filter bar */
.filter-bar {
  display: flex;
  align-items: flex-end;
  flex-wrap: wrap;
  gap: 1rem;
  max-width: 1200px;
  margin: 0 auto 1.5rem auto;
  padding: 0 3rem;
}

.filter-dates {
  display: flex;
  gap: 1rem;
}

.filter-date {
  display: flex;
  flex-direction: column;
}
.filter-date label {
  font-size: 0.7rem;
  margin-bottom: 0.2rem;
  color: #666;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.filter-bar input[type="date"],
.filter-bar select {
  height: 36px;
  font-size: 0.85rem;
  padding: 0 0.6rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #f9f9f9;
  color: #333;
  box-sizing: border-box;
}

.filter-bar input[type="date"]:hover,
.filter-bar select:hover {
  border-color: #999;
}

.log-workout-btn {
  margin-left: auto;
  text-decoration: none;
  border: 1px solid #444;
  padding: 0.4rem 1rem;
  font-size: 0.8rem;
  background-color: #fff;
  color: #000;
  letter-spacing: 0.05em;
  border-radius: 6px;
  height: 36px;
  display: flex;
  align-items: center;
  font-weight: bold;
}
.log-workout-btn:hover {
  background-color: #eee;
}

body.dark-mode .filter-bar input[type="date"],
body.dark-mode .filter-bar select {
  background-color: #222;
  color: #eee;
  border-color: #555;
}
body.dark-mode .log-workout-btn {
  background-color: #222;
  color: #eee;
  border-color: #777;
}
body.dark-mode .log-workout-btn:hover {
  background-color: #444;
}

/* TABLE STYLING */
.workouts-table {
  width: 100%;
  border-collapse: collapse;
  margin: 0 auto 2rem auto;
  max-width: 1200px;
  padding: 0 3rem;
}

.workouts-table th {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #555;
  border-bottom: 1px solid #ddd;
  padding: 0.75rem 0.5rem;
  text-align: left;
  cursor: pointer;
}

.workouts-table th span {
  margin-left: 4px;
  font-size: 0.75rem;
}

.workouts-table td {
  font-size: 0.85rem;
  padding: 0.9rem 0.5rem;
  border-bottom: 1px solid #eee;
  vertical-align: top;
}

.actions {
  white-space: nowrap;
  text-align: right;
}

.icon-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.1rem;
  margin-left: 0.5rem;
  color: #000;
  text-decoration: none;
}
.icon-btn:hover {
  opacity: 0.7;
}

body.dark-mode .workouts-table th,
body.dark-mode .workouts-table td {
  color: #eee;
  border-color: #555;
}
body.dark-mode .icon-btn {
  color: #eee;
}

@media (max-width: 425px) {
  .sub-nav {
    margin-top: 5rem;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
    padding: 0 1rem;
  }

  .tab {
    font-size: 0.85rem;
    padding: 0.4rem 0;
    white-space: nowrap;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
    padding: 0 1rem;
    gap: 0.75rem;
  }

  .filter-dates {
    display: flex;
    flex-direction: row;
    gap: 0.75rem;
    justify-content: space-between;
  }

  .filter-dates .filter-date {
    flex: 1;
  }

  .log-workout-btn {
    margin-left: 0;
    justify-content: center;
  }

  .workouts-table {
    display: block;
    overflow-x: auto;
    white-space: nowrap;
    padding: 0 1rem;
  }

  .workouts-table th,
  .workouts-table td {
    font-size: 0.8rem;
    padding: 0.5rem;
  }
}
</style>
