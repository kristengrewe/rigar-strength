<template>
  <form class="log-workout-form" @submit.prevent="submitForm">

    <div class="row">
      <div class="field">
        <label>Date</label>
        <input type="date" v-model="localWorkout.date" required />
      </div>
      <div class="field">
        <label>Lift</label>
        <select v-model="localWorkout.liftId" required>
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

    <div class="row second">
      <div class="field">
        <label>Sets</label>
        <input type="number" v-model.number="localWorkout.sets" min="1" />
      </div>
      <div class="field">
        <label>Reps</label>
        <input type="number" v-model.number="localWorkout.reps" min="1" />
      </div>
      <div class="field">
        <label>Weight</label>
        <input
          type="number"
          v-model.number="localWorkout.maxWeight"
          min="0"
        />
      </div>
    </div>

    <div class="field">
      <label>Notes</label>
      <textarea v-model="localWorkout.notes" placeholder="Optional"></textarea>
    </div>

    <!-- Buttons -->
    <div class="buttons">
      <button type="submit" class="save-btn">✔ SAVE</button>
      <button type="button" class="clear-btn" @click="clearForm">✕ CLEAR FORM</button>
    </div>
  </form>
</template>

<script>
import ResourceService from "../services/ResourceService";

export default {
  name: "WorkoutForm",
  props: {
    personId: { type: Number, required: true },
    initialWorkout: { type: Object, default: null }
  },
  data() {
    return {
      lifts: [],
      localWorkout: {
        personId: this.personId,
        liftId: "",
        date: "",
        sets: "",
        reps: "",
        maxWeight: "",
        notes: ""
      }
    };
  },
  watch: {
    initialWorkout: {
      immediate: true,
      handler(val) {
        if (val) {
          this.localWorkout = { ...val };
        }
      }
    }
  },
  created() {
    ResourceService.getLifts().then((res) => {
      this.lifts = res.data;
    });
  },
  methods: {
    submitForm() {
      this.$emit("submit-workout", { ...this.localWorkout });
    },
    clearForm() {
      this.localWorkout = {
        personId: this.personId,
        liftId: "",
        date: "",
        sets: "",
        reps: "",
        maxWeight: "",
        notes: ""
      };
    }
  }
};
</script>

<style scoped>
.log-workout-form {
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
}

.form-title {
  text-align: center;
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
}

.row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}
.row.second {
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
  gap: 1rem;
  margin-top: 1rem;
  justify-content: center;
}
.save-btn {
  background: #000;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 0.9rem;
}
.save-btn:hover {
  background: #444;
}
.clear-btn {
  background: none;
  border: 1px solid #000;
  color: #000;
  border-radius: 6px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 0.9rem;
}
.clear-btn:hover {
  opacity: 0.8;
}
</style>
