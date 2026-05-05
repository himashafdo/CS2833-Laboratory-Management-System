// theme.js — include in every page
(function () {
  const THEME_KEY = "theme";

  function applyTheme(theme) {
    document.documentElement.setAttribute("data-theme", theme);

    const btn = document.getElementById("theme-btn");
    if (btn) {
      btn.textContent = theme === "dark" ? "☀️" : "🌙";
      btn.title =
        theme === "dark" ? "Switch to light mode" : "Switch to dark mode";
    }
  }

  const saved = localStorage.getItem(THEME_KEY) || "light";

  // Apply immediately before page fully loads
  applyTheme(saved);

  document.addEventListener("DOMContentLoaded", function () {
    const btn = document.getElementById("theme-btn");

    // Update button icon after DOM loads
    applyTheme(localStorage.getItem(THEME_KEY) || "light");

    if (btn) {
      btn.addEventListener("click", function () {
        const current =
          document.documentElement.getAttribute("data-theme") || "light";

        const next = current === "dark" ? "light" : "dark";

        localStorage.setItem(THEME_KEY, next);
        applyTheme(next);
      });
    }
  });
})();
