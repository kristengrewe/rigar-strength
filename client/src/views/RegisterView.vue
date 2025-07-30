<template>
  <main class="register-section">
    <form class="register-form" @submit.prevent="register">
      <div class="register-grid">
        <div class="field full-width">
          <label>Username</label>
          <input v-model="user.username" required />
        </div>

        <div class="field">
          <label>Password</label>
          <input type="password" v-model="user.password" required />
        </div>
        <div class="field">
          <label>Confirm Password</label>
          <input type="password" v-model="user.confirmPassword" required />
        </div>

        <div class="field">
          <label>First Name</label>
          <input v-model="user.firstName" required />
        </div>
        <div class="field">
          <label>Last Name</label>
          <input v-model="user.lastName" required />
        </div>

        <div class="field">
          <label>Birthdate</label>
          <input type="date" v-model="user.birthdate" required />
        </div>
        <div class="field">
          <label>Email</label>
          <input type="email" v-model="user.email" required />
        </div>
      </div>

      <button type="submit" class="register-button">Create Account</button>
      <hr />
      <p class="login-text">
        Have an account?
        <router-link class="login-link" :to="{ name: 'login' }">Sign in!</router-link>
      </p>
    </form>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "RegisterView",
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        firstName: "",
        lastName: "",
        birthdate: "",
        email: "",
        role: "ROLE_USER"
      }
    };
  },
  methods: {
    error(msg) {
      alert(msg);
    },
    success(msg) {
      alert(msg);
    },
    register() {
      if (this.user.password !== this.user.confirmPassword) {
        this.error("Password & Confirm Password do not match");
        return;
      }
      authService
        .register(this.user)
        .then((response) => {
          if (response.status === 201) {
            this.success("Thank you for registering, please sign in.");
            this.$router.push({ name: "login" });
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) this.error(error);
          else if (response.status === 400) {
            if (response.data.errors) {
              let msg = "Validation error: ";
              for (let err of response.data.errors) {
                msg += `'${err.field}': ${err.defaultMessage}. `;
              }
              this.error(msg);
            } else {
              this.error(response.data.message);
            }
          } else {
            this.error(response.data.message);
          }
        });
    }
  }
};
</script>

<style scoped>
.register-section {
  margin-top: 0rem;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.register-form {
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 12px;
  max-width: 600px;
  width: 100%;
  padding: 1.5rem 1.5rem 0.75rem;
  box-shadow: 0 4px 8px rgba(0,0,0,0.08);
}
.register-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.75rem 1rem;
  margin-bottom: 1rem;
}
.field {
  display: flex;
  flex-direction: column;
}
.field label {
  margin-bottom: 0.3rem;
}
.field input {
  font-size: 1rem;
  padding: 0.6rem;
  border: 1px solid #abb8c3;
  border-radius: 6px;
}
.field input:focus {
  border-color: #000;
}

.full-width {
  grid-column: 1 / -1;
}

.register-button {
  margin-top: 1rem;
  padding: 0.75rem;
  font-size: 1rem;
  font-weight: bold;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
  width: 100%;
}
.register-button:hover {
  background-color: #444;
}
.login-text {
  margin-top: 1rem;
  font-size: 0.9rem;
  text-align: center;
}
.login-link {
  font-weight: 600;
  color: #000;
  text-decoration: none;
  border-bottom: 2px solid transparent;
}
.login-link:hover {
  border-bottom: 2px solid #000;
}
@media(max-width:600px) {
  .register-grid {
    grid-template-columns: 1fr;
  }
}
</style>
