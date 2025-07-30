<template>
  <div id="app">
    <!-- HEADER -->
    <header class="header">
      <div class="logo">
        <router-link :to="{ name: 'home' }">
          <img :src="logo" alt="RIGAR Performance Logo" class="logo-img" />
        </router-link>
        <div class="welcome-message">WELCOME TO RIGAR STRENGTH</div>
      </div>
      <nav class="nav">
        <router-link class="nav-button" :to="{ name: 'home' }">HOME</router-link>
        <button class="nav-button" @click="openAbout">ABOUT</button>
        <router-link
          v-if="$store.state.token"
          class="nav-button"
          :to="{ name: 'logout' }"
        >LOG OUT</router-link>
        <router-link
          v-else
          class="nav-button"
          :to="{ name: 'login' }"
        >LOG IN</router-link>
        <button class="dark-toggle" @click="toggleDarkMode">
          {{ isDarkMode ? '☼' : '☀' }}
        </button>
      </nav>
    </header>

    <!-- MAIN PAGE CONTENT -->
    <main>
      <router-view />
    </main>

    <!-- FOOTER -->
    <footer class="site-footer">
      <p>&copy; 2025 RIGAR STRENGTH. All rights reserved.</p>
      <ul class="footer-nav">
        <li>
          <a href="https://rigarperformance.com/privacy-policy/" target="_blank">Privacy Policy</a>
        </li>
      </ul>
    </footer>
  </div>
</template>

<script>
import Logo from "@/assets/rigar-logo.png";

export default {
  name: "App",
  data() {
    return { 
      logo: Logo,
      isDarkMode: false
    };
  },
  methods: {
    openAbout() {
      window.open("https://rigarperformance.com/", "_blank");
    },
    toggleDarkMode() {
      this.isDarkMode = !this.isDarkMode;
      if (this.isDarkMode) document.body.classList.add("dark-mode");
      else document.body.classList.remove("dark-mode");
    }
  }
};
</script>

<style>
.header {
  display:flex;align-items:center;justify-content:space-between;
  background:#000;color:#fff;padding:1rem 2rem;width:100vw;height:70px;
  position:fixed;top:0;left:0;z-index:1000;
}
.logo {display:flex;align-items:center;gap:1rem;}
.logo-img {height:40px;width:auto;}
.welcome-message {font-size:1rem;font-weight:600;}
.nav {display:flex;align-items:center;gap:1.5rem;}
.nav-button {
  color:#fff;font-weight:bold;background:none;border:none;cursor:pointer;
  font-family:inherit;font-size:1rem;position:relative;text-decoration:none;
}
.nav-button::after {
  content:'';position:absolute;left:0;bottom:-2px;width:100%;height:2px;
  background-color:transparent;transition:background-color 0.2s;
}
.nav-button:hover::after,
.nav-button.router-link-exact-active::after {background-color:#fff;}
.dark-toggle {background:none;border:none;font-size:1.4rem;color:#fff;cursor:pointer;}

main {
  padding-top:60px;
  padding-bottom:60px;
  min-height:calc(100vh - 140px);
}

.site-footer {
  background:#000;padding:0.5rem 1rem;text-align:center;
  border-top:1px solid #ccc;color:#fff;width:100vw;
  position:fixed;bottom:0;left:0;z-index:1000;font-size:0.8rem;
}
.footer-nav {
  list-style:none;display:flex;justify-content:center;gap:1rem;margin-top:0.3rem;
}
.footer-nav a {text-decoration:none;font-size:0.75rem;color:#fff;}

@media(max-width:425px){
  .header {flex-direction:column;height:auto;padding:1rem;gap:0.5rem;text-align:center;}
  .nav {gap:1rem;}
}
</style>
