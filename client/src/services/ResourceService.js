import axios from 'axios';

let lifts = [];

let workouts = [];

export default {

  getWorkouts() {
    return axios.get('/api/workouts')
  },

  getLifts() {
    return axios.get('/api/lifts')
  },

  createWorkout(workout) {
    return axios.post('/api/workouts', workout);
  },

  updateWorkout(workoutId, workout) {
    return axios.put(`/api/workouts/${workoutId}`, workout);
  },

  deleteWorkout(workoutId) {
    return axios.delete(`/api/workouts/${workoutId}`);
  },

  getMembers() {
    return axios.get('/api/members');
  }

};