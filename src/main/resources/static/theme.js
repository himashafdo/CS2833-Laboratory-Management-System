// theme.js
(function () {
  const THEME_KEY = "theme";

  // Inject toggle styles
  const s = document.createElement("style");
  s.textContent = `
    #theme-btn {
  width: 52px; height: 28px; border-radius: 99px;
  border: none; cursor: pointer; position: relative;
  background: #334155; transition: background 0.3s; border: 2px solid #475569;
  flex-shrink: 0;
  transform: scale(0.85);
}
    #theme-btn.light { background: #f97316; border-color: #ea6c0a; }
    #theme-btn::after {
  content: ''; position: absolute;
  width: 22px; height: 22px; border-radius: 50%;
  background: white; top: 1px; left: 1px;
  transition: transform 0.3s cubic-bezier(.4,0,.2,1);
  box-shadow: 0 1px 4px rgba(0,0,0,0.25);
}
    #theme-btn.light::after { transform: translateX(24px); }
  `;
  document.head.appendChild(s);

  function applyTheme(theme) {
    document.documentElement.setAttribute("data-theme", theme);
    const btn = document.getElementById("theme-btn");
    if (btn) {
      btn.classList.toggle("light", theme === "light");
      btn.title =
        theme === "dark" ? "Switch to light mode" : "Switch to dark mode";
      btn.textContent = "";
    }
  }

  const saved = localStorage.getItem(THEME_KEY) || "light";
  applyTheme(saved);

  document.addEventListener("DOMContentLoaded", function () {
    applyTheme(localStorage.getItem(THEME_KEY) || "light");
    const btn = document.getElementById("theme-btn");
    if (btn) {
      btn.addEventListener("click", function () {
        const next =
          document.documentElement.getAttribute("data-theme") === "dark"
            ? "light"
            : "dark";
        localStorage.setItem(THEME_KEY, next);
        applyTheme(next);
      });
    }
  });
})();

document
  .querySelectorAll(".search-box")
  .forEach((el) => (el.style.display = "none"));
