<template>
  <div class="log-workout-wrapper">
    <div class="form-header">
      <h2 class="form-title">Edit Workout</h2>
      <button class="close-btn" @click="goBack">✕</button>
    </div>

    <div v-if="isLoading" class="loading">Loading workout...</div>
    <div v-else-if="!workout" class="loading">Workout not found.</div>
    <div v-else>
      <WorkoutForm
        :personId="userId"
        :initialWorkout="workout"
        @submit-workout="handleSubmit"
      />
    </div>
  </div>
</template>

<script>
import WorkoutForm from "../components/WorkoutForm.vue";
import ResourceService from "../services/ResourceService";

export default {
  name: "UpdateWorkoutView",
  components: { WorkoutForm },
  data() {
    return {
      workout: null,
      isLoading: true,
    };
  },
  computed: {
    workoutId() {
      return this.$route.params.workoutId;
    },
    userId() {
      return this.$store.state.user?.id || null;
    },
  },
  created() {
    this.fetchWorkout();
  },
  methods: {
    fetchWorkout() {
      this.isLoading = true;
      ResourceService.getWorkouts()
        .then((res) => {
          const found = res.data.find(
            (w) => String(w.workoutId) === String(this.workoutId)
          );
          if (found) {
            this.workout = {
              personId: found.personId,
              liftId: found.liftId,
              date: found.date,
              sets: found.sets,
              reps: found.reps,
              maxWeight: found.maxWeight,
              notes: found.notes || "",
            };
          }
        })
        .catch((err) => {
          console.error("Failed to load workout:", err);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    handleSubmit(payload) {
      ResourceService.updateWorkout(this.workoutId, payload).then(() => {
        this.$router.push({ name: "workouts" });
      });
    },
    goBack() {
      this.$router.push({ name: "workouts" });
    },
  },
};
</script>

<style>
.log-workout-wrapper {
  padding: 2rem;
  max-width: 700px;
  margin: 0 auto;
}

.form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.form-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.3rem;
  cursor: pointer;
}
.close-btn:hover {
  opacity: 0.7;
}

.loading {
  text-align: center;
  margin-top: 2rem;
  font-style: italic;
}

/* DARK MODE */
body.dark-mode {
  background-color: #000;
}

body.dark-mode .form-title {
  color: #fff;
}

body.dark-mode .close-btn {
  color: #ddd;
}
body.dark-mode .close-btn:hover {
  color: #fff;
}

body.dark-mode .log-workout-form,
body.dark-mode .form-container {
  background-color: #222;
  border: 1px solid #555;
  border-radius: 8px;
}

body.dark-mode .field label {
  color: #fff;
}
body.dark-mode .field input,
body.dark-mode .field select,
body.dark-mode .field textarea {
  background-color: #333; 
  color: #fff;            
  border: 1px solid #555;
}
body.dark-mode .field input:hover,
body.dark-mode .field select:hover,
body.dark-mode .field textarea:hover {
  border-color: #777;
}

body.dark-mode .save-btn {
  background-color: #444;
  color: #fff;
  border: none;
}
body.dark-mode .save-btn:hover {
  background-color: #666;
}

body.dark-mode .clear-btn {
  background: none;
  border: 1px solid #fff; 
  color: #fff;            
}
body.dark-mode .clear-btn:hover {
  background-color: #444;
}

@media (max-width: 425px) {
  .log-workout-wrapper {
    padding: 1rem;
    margin-top: 4rem;
  }

  .form-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }

  .form-title {
    font-size: 1.1rem;
    font-weight: 600;
    margin: 0;
    text-align: left;
  }

  .close-btn {
    font-size: 1.4rem;
    background: none;
    border: none;
    cursor: pointer;
    margin: 0;
    padding: 0.25rem;
  }

  .log-workout-form .row {
    display: grid;
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .log-workout-form .row.second-row {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 0.5rem;
  }

  .log-workout-form .field {
    display: flex;
    flex-direction: column;
    min-width: 0;
  }

  .log-workout-form .field label {
    font-size: 0.7rem;
    margin-bottom: 0.2rem;
    word-wrap: break-word;
  }

  .log-workout-form .field input,
  .log-workout-form .field select,
  .log-workout-form .field textarea {
    font-size: 0.85rem;
    padding: 0.4rem;
    box-sizing: border-box;
    width: 100%;
  }

  .buttons {
    flex-direction: column;
    gap: 0.5rem;
    margin-top: 1rem;
  }

  .save-btn,
  .clear-btn {
    width: 100%;
    font-size: 0.9rem;
    padding: 0.6rem;
  }
}
</style>