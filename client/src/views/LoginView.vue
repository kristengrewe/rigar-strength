<template>
  <section class="login-section">
    <form class="login-form" @submit.prevent="login">
      <div class="login-fields">
        <label for="username">Username</label>
        <input
          id="username"
          v-model="user.username"
          required
          autofocus
        />

        <label for="password">Password</label>
        <input
          id="password"
          type="password"
          v-model="user.password"
          required
        />

        <button type="submit" class="login-button">Sign In</button>
      </div>
      <hr />
      <p class="register-text">
        Need an account?
        <router-link class="register-link" :to="{ name: 'register' }">
          Register!
        </router-link>
      </p>
    </form>
  </section>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "LoginView",
  data() {
    return {
      user: { username: "", password: "" }
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status === 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) alert(error);
          else if (response.status === 401) alert("Invalid username and password!");
          else alert(response.message);
        });
    }
  }
};
</script>

<style scoped>
.login-section {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}
.login-form {
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 12px;
  max-width: 400px;
  width: 100%;
  padding: 2rem 1.5rem;
  box-shadow: 0 4px 8px rgba(0,0,0,0.08);
}
.login-fields {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}
.login-fields label {
  font-weight: 600;
  margin-bottom: 0.2rem;
}
.login-fields input {
  font-size: 1rem;
  padding: 0.75rem;
  border: 1px solid #abb8c3;
  border-radius: 6px;
}
.login-fields input:focus {
  border-color: #000;
}
.login-button {
  margin-top: 0.75rem;
  padding: 0.75rem;
  font-size: 1rem;
  font-weight: bold;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.login-button:hover {
  background-color: #444;
}
.register-text {
  margin-top: 1rem;
  font-size: 0.9rem;
  text-align: center;
}
.register-link {
  font-weight: 600;
  color: #000;
  text-decoration: none;
  border-bottom: 2px solid transparent;
}
.register-link:hover {
  border-bottom: 2px solid #000;
}

@media (max-width: 425px) {
  .login-section {
    margin-top: 5rem;
    padding: 0 1rem;
    width: 100%;
    box-sizing: border-box;
  }

  .login-form {
    max-width: 100%;
    width: 100%;
    padding: 1.5rem 1rem;
    border-radius: 8px;
    margin: 0 auto;
    box-shadow: none;
  }

  .login-fields {
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
  }

  .login-fields label {
    font-size: 0.9rem;
  }

  .login-fields input {
    font-size: 0.95rem;
    padding: 0.65rem;
  }

  .login-button {
    margin-top: 0.75rem;
    padding: 0.75rem;
    font-size: 1rem;
    font-weight: bold;
    width: 100%;
  }

  .register-text {
    margin-top: 1rem;
    font-size: 0.9rem;
    text-align: center;
  }
}
</style>
