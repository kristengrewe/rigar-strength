<template>
  <div class="log-workout-wrapper">
    <!-- Header-->
    <div class="form-header">
      <h2 class="form-title">Log Workout</h2>
      <button class="close-btn" @click="goBack">✕</button>
    </div>

    <!-- Form Row 1 -->
    <div class="form-container">
      <form class="log-workout-form" @submit.prevent="submitWorkout">
        <div class="row">
          <div class="field">
            <label>Date</label>
            <input type="date" v-model="workout.date" required />
          </div>
          <div class="field">
            <label>Lift</label>
            <select v-model="workout.liftId" required>
              <option disabled value="">Select lift...</option>
              <option
                v-for="lift in lifts"
                :key="lift.liftId"
                :value="lift.liftId"
              >
                {{ lift.liftName }}
              </option>
            </select>
          </div>
        </div>

        <!-- Row 2: Sets, Reps, Weight -->
        <div class="row second-row">
          <div class="field">
            <label>Sets</label>
            <input type="number" v-model.number="workout.sets" min="1" required />
          </div>
          <div class="field">
            <label>Reps</label>
            <input type="number" v-model.number="workout.reps" min="1" required />
          </div>
          <div class="field">
            <label>Weight</label>
            <input type="number" v-model.number="workout.maxWeight" min="0" />
          </div>
        </div>

        <!-- Notes -->
        <div class="field">
          <label>Notes</label>
          <textarea v-model="workout.notes" placeholder="Optional"></textarea>
        </div>

        <!-- Buttons -->
        <div class="buttons">
          <button type="submit" class="save-btn">✔ SAVE</button>
          <button type="button" class="clear-btn" @click="clearForm">
            ✕ CLEAR FORM
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import ResourceService from "../services/ResourceService";

export default {
  name: "CreateWorkoutView",
  data() {
    return {
      lifts: [],
      workout: {
        personId: null,
        liftId: "",
        date: "",
        sets: "",
        reps: "",
        maxWeight: "",
        notes: ""
      }
    };
  },
  created() {
    const user = this.$store.state.user;
    if (user) {
      this.workout.personId = user.id;
    }
    ResourceService.getLifts().then((res) => {
      this.lifts = res.data;
    });
  },
  methods: {
    submitWorkout() {
      const user = this.$store.state.user;
      if (!user || !user.id) {
        alert("User not found. Please log in again.");
        return;
      }

      const payload = {
        personId: user.id,
        liftId: this.workout.liftId,
        date: this.workout.date,
        sets: this.workout.sets,
        reps: this.workout.reps,
        maxWeight: this.workout.maxWeight || 0,
        notes: this.workout.notes
      };

      ResourceService.createWorkout(payload)
        .then(() => {
          this.$router.push({ name: "workouts" });
        })
        .catch((err) => {
          console.error("Failed to create workout:", err);
          alert("There was a problem saving your workout.");
        });
    },
    clearForm() {
      const user = this.$store.state.user;
      this.workout = {
        personId: user ? user.id : null,
        liftId: "",
        date: "",
        sets: "",
        reps: "",
        maxWeight: "",
        notes: ""
      };
    },
    goBack() {
      this.$router.push({ name: "workouts" });
    }
  }
};
</script>

<style scoped>
.log-workout-wrapper {
  position: relative;
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

.form-container {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 1.5rem;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
}

/* form fields */
.log-workout-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}
.row.second-row {
  grid-template-columns: 1fr 1fr 1fr;
}

.field {
  display: flex;
  flex-direction: column;
}
.field label {
  font-size: 0.75rem;
  font-weight: 600;
  margin-bottom: 0.3rem;
  text-transform: uppercase;
}
.field input,
.field select,
.field textarea {
  height: 36px;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 0.4rem;
  font-size: 0.9rem;
  background-color: #f9f9f9;
  color: #333;
  box-sizing: border-box;
}
.field textarea {
  height: auto;
  min-height: 60px;
  resize: none;
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 0.5rem;
}

.save-btn {
  background: #000;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 0.8rem;
}
.save-btn:hover {
  background: #333;
}
.clear-btn {
  background: none;
  border: 1px solid #000;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 0.8rem;
}
.clear-btn:hover {
  background: #eee;
}

/* Dark mode */
body.dark-mode .form-title {
  color: #fff;
}
body.dark-mode .close-btn {
  color: #fff;
}
body.dark-mode .close-btn:hover {
  color: #ccc;
}
body.dark-mode .form-container {
  background-color: #222;
  border-color: #555;
}
body.dark-mode .log-workout-form {
  background-color: #222;
  border: none;
  box-shadow: none;
}
body.dark-mode .field input,
body.dark-mode .field select,
body.dark-mode .field textarea {
  background-color: #333;
  color: #fff;
  border: 1px solid #555;
}
body.dark-mode .save-btn {
  background-color: #444;
  color: #fff;
  border-color: #777;
}
body.dark-mode .clear-btn {
  background: none;
  border: 1px solid #fff;
  color: #fff;
}
body.dark-mode .clear-btn:hover {
  background-color: #555;
}

@media (max-width: 425px) {
  .log-workout-wrapper {
    padding: 1rem;
    margin-top: 4rem;
  }

  .form-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }

  .form-title {
    font-size: 1.1rem;
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

  .form-container {
    padding: 1rem;
  }

  .row {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .row.second-row {
    grid-template-columns: 1fr;
    gap: 0.75rem;
  }

  .field label {
    font-size: 0.75rem;
  }

  .field input,
  .field select,
  .field textarea {
    font-size: 0.85rem;
    padding: 0.5rem;
  }

  .buttons {
    flex-direction: column;
    gap: 0.5rem;
  }

  .save-btn,
  .clear-btn {
    width: 100%;
    font-size: 0.9rem;
    padding: 0.6rem;
  }
}

</style>
