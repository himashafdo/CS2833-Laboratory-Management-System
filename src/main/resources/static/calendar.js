/**
 * LabMS Mini Calendar
 * Include this file in any page that has a topbar-date element.
 * Usage: <script src="/calendar.js"></script>
 * Requires: token in sessionStorage, /api/reservations/my endpoint
 */

(function () {
  const API = "http://localhost:8080/api";

  // ── STYLES ──────────────────────────────────────────────────
  const style = document.createElement("style");
  style.textContent = `
    .cal-overlay {
      position: fixed; inset: 0; z-index: 999;
      background: transparent;
    }
    .cal-popup {
      position: fixed;
      background: #ffffff;
      border: 1px solid #e2e8f0;
      border-radius: 18px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.15), 0 4px 20px rgba(0,0,0,0.08);
      width: 320px;
      z-index: 1000;
      overflow: hidden;
      animation: calFadeIn 0.15s ease;
    }
    @keyframes calFadeIn {
      from { opacity: 0; transform: translateY(-8px) scale(0.97); }
      to   { opacity: 1; transform: translateY(0) scale(1); }
    }
    .cal-header {
      background: linear-gradient(135deg, #f97316, #ea6c0a);
      padding: 16px 20px;
      display: flex; align-items: center; justify-content: space-between;
    }
    .cal-month-label {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 15px; font-weight: 700; color: white;
    }
    .cal-nav {
      width: 28px; height: 28px; border-radius: 8px;
      background: rgba(255,255,255,0.2); border: none; cursor: pointer;
      color: white; display: flex; align-items: center; justify-content: center;
      font-size: 14px; transition: background 0.2s;
    }
    .cal-nav:hover { background: rgba(255,255,255,0.35); }
    .cal-body { padding: 16px 16px 8px; }
    .cal-weekdays {
      display: grid; grid-template-columns: repeat(7, 1fr);
      margin-bottom: 6px;
    }
    .cal-weekday {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 11px; font-weight: 600; color: #94a3b8;
      text-align: center; padding: 4px 0;
    }
    .cal-days {
      display: grid; grid-template-columns: repeat(7, 1fr);
      gap: 2px;
    }
    .cal-day {
      aspect-ratio: 1; border-radius: 8px;
      display: flex; flex-direction: column; align-items: center; justify-content: center;
      cursor: pointer; position: relative; transition: all 0.15s;
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 13px; font-weight: 500; color: #1a1a2e;
    }
    .cal-day:hover { background: #fff7ed; }
    .cal-day.empty { cursor: default; }
    .cal-day.empty:hover { background: transparent; }
    .cal-day.today {
      background: #fff7ed;
      font-weight: 700; color: #f97316;
      border: 1.5px solid #f97316;
    }
    .cal-day.selected {
      background: #f97316 !important;
      color: white !important;
      font-weight: 700;
    }
    .cal-day.has-res { font-weight: 600; }
    .cal-dots {
      display: flex; gap: 2px; position: absolute;
      bottom: 3px; left: 50%; transform: translateX(-50%);
    }
    .cal-dot {
      width: 4px; height: 4px; border-radius: 50%;
    }
    .cal-dot.equipment { background: #f97316; }
    .cal-dot.lab       { background: #3b82f6; }

    .cal-detail {
      border-top: 1px solid #e2e8f0;
      padding: 12px 16px;
      max-height: 160px;
      overflow-y: auto;
    }
    .cal-detail-title {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 11px; font-weight: 700; color: #94a3b8;
      text-transform: uppercase; letter-spacing: 0.06em;
      margin-bottom: 8px;
    }
    .cal-detail-item {
      display: flex; align-items: center; gap: 8px;
      padding: 6px 0; border-bottom: 1px solid #f1f5f9;
      font-family: 'Plus Jakarta Sans', sans-serif;
    }
    .cal-detail-item:last-child { border-bottom: none; }
    .cal-detail-dot {
      width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0;
    }
    .cal-detail-dot.equipment { background: #f97316; }
    .cal-detail-dot.lab       { background: #3b82f6; }
    .cal-detail-name { font-size: 13px; font-weight: 600; color: #1a1a2e; flex: 1; min-width: 0; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .cal-detail-time { font-size: 11px; color: #64748b; flex-shrink: 0; }
    .cal-detail-empty { font-size: 13px; color: #94a3b8; text-align: center; padding: 8px 0; }

    .cal-legend {
      padding: 10px 16px 14px;
      display: flex; gap: 16px; align-items: center;
    }
    .cal-legend-item {
      display: flex; align-items: center; gap: 5px;
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 11px; color: #64748b;
    }
    .cal-legend-dot {
      width: 8px; height: 8px; border-radius: 50%;
    }
    .cal-legend-dot.equipment { background: #f97316; }
    .cal-legend-dot.lab       { background: #3b82f6; }
  `;
  document.head.appendChild(style);

  // ── STATE ────────────────────────────────────────────────────
  let reservations = [];
  let currentYear = new Date().getFullYear();
  let currentMonth = new Date().getMonth();
  let selectedDate = null;
  let popupEl = null;
  let overlayEl = null;
  let isOpen = false;

  // ── FETCH RESERVATIONS ───────────────────────────────────────
  async function fetchReservations() {
    const token = sessionStorage.getItem("token");
    if (!token) return;
    try {
      const res = await fetch(`${API}/reservations/my`, {
        headers: { Authorization: "Bearer " + token },
      });
      reservations = await res.json();
    } catch (e) {
      reservations = [];
    }
  }

  // ── HELPERS ──────────────────────────────────────────────────
  function getReservationsForDate(year, month, day) {
    return reservations.filter((r) => {
      if (r.status === "CANCELLED") return false;
      const d = new Date(r.startTime);
      return (
        d.getFullYear() === year &&
        d.getMonth() === month &&
        d.getDate() === day
      );
    });
  }

  function getDotTypes(year, month, day) {
    const dayRes = getReservationsForDate(year, month, day);
    const hasEquipment = dayRes.some((r) => r.equipment != null);
    const hasLab = dayRes.some((r) => r.equipment == null && r.lab != null);
    return { hasEquipment, hasLab, any: dayRes.length > 0 };
  }

  // ── BUILD CALENDAR HTML ──────────────────────────────────────
  function buildCalendar() {
    const today = new Date();
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
    const monthNames = [
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December",
    ];

    let daysHTML = "";
    // Empty cells before first day
    for (let i = 0; i < firstDay; i++) {
      daysHTML += `<div class="cal-day empty"></div>`;
    }
    // Day cells
    for (let d = 1; d <= daysInMonth; d++) {
      const isToday =
        today.getFullYear() === currentYear &&
        today.getMonth() === currentMonth &&
        today.getDate() === d;
      const isSelected =
        selectedDate &&
        selectedDate.year === currentYear &&
        selectedDate.month === currentMonth &&
        selectedDate.day === d;
      const dots = getDotTypes(currentYear, currentMonth, d);

      let dotsHTML = "";
      if (dots.hasEquipment)
        dotsHTML += `<div class="cal-dot equipment"></div>`;
      if (dots.hasLab) dotsHTML += `<div class="cal-dot lab"></div>`;

      const classes = [
        "cal-day",
        isToday ? "today" : "",
        isSelected ? "selected" : "",
        dots.any ? "has-res" : "",
      ]
        .filter(Boolean)
        .join(" ");

      daysHTML += `
        <div class="${classes}" onclick="window._calSelectDay(${d})">
          ${d}
          ${dotsHTML ? `<div class="cal-dots">${dotsHTML}</div>` : ""}
        </div>`;
    }

    // Detail panel
    let detailHTML = "";
    if (selectedDate) {
      const dayRes = getReservationsForDate(
        selectedDate.year,
        selectedDate.month,
        selectedDate.day,
      );
      const label = `${selectedDate.day} ${monthNames[selectedDate.month]}`;
      detailHTML = `
        <div class="cal-detail">
          <div class="cal-detail-title">${label}</div>
          ${
            dayRes.length === 0
              ? `<div class="cal-detail-empty">No reservations this day</div>`
              : dayRes
                  .map((r) => {
                    const name = r.equipment
                      ? r.equipment.name
                      : r.lab
                        ? r.lab.labName
                        : "Unknown";
                    const type = r.equipment ? "equipment" : "lab";
                    const start = new Date(r.startTime).toLocaleTimeString(
                      "en-GB",
                      { hour: "2-digit", minute: "2-digit" },
                    );
                    const end = new Date(r.endTime).toLocaleTimeString(
                      "en-GB",
                      { hour: "2-digit", minute: "2-digit" },
                    );
                    return `
                  <div class="cal-detail-item">
                    <div class="cal-detail-dot ${type}"></div>
                    <div class="cal-detail-name">${name}</div>
                    <div class="cal-detail-time">${start}–${end}</div>
                  </div>`;
                  })
                  .join("")
          }
        </div>`;
    }

    return `
      <div class="cal-header">
        <button class="cal-nav" onclick="window._calNav(-1)">&#8249;</button>
        <div class="cal-month-label">${monthNames[currentMonth]} ${currentYear}</div>
        <button class="cal-nav" onclick="window._calNav(1)">&#8250;</button>
      </div>
      <div class="cal-body">
        <div class="cal-weekdays">
          ${["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"].map((d) => `<div class="cal-weekday">${d}</div>`).join("")}
        </div>
        <div class="cal-days">${daysHTML}</div>
      </div>
      ${detailHTML}
      <div class="cal-legend">
        <div class="cal-legend-item"><div class="cal-legend-dot equipment"></div> Equipment booking</div>
        <div class="cal-legend-item"><div class="cal-legend-dot lab"></div> Lab reservation</div>
      </div>`;
  }

  // ── OPEN / CLOSE ─────────────────────────────────────────────
  function openCalendar(anchorEl) {
    if (isOpen) {
      closeCalendar();
      return;
    }
    isOpen = true;

    // Overlay to catch outside clicks
    overlayEl = document.createElement("div");
    overlayEl.className = "cal-overlay";
    overlayEl.addEventListener("click", closeCalendar);
    document.body.appendChild(overlayEl);

    // Popup
    popupEl = document.createElement("div");
    popupEl.className = "cal-popup";
    popupEl.addEventListener("click", (e) => e.stopPropagation());
    document.body.appendChild(popupEl);

    // Position below anchor
    const rect = anchorEl.getBoundingClientRect();
    const top = rect.bottom + 8;
    const right = window.innerWidth - rect.right;
    popupEl.style.top = top + "px";
    popupEl.style.right = right + "px";

    refreshPopup();
  }

  function closeCalendar() {
    if (!isOpen) return;
    isOpen = false;
    selectedDate = null;
    if (popupEl) {
      popupEl.remove();
      popupEl = null;
    }
    if (overlayEl) {
      overlayEl.remove();
      overlayEl = null;
    }
  }

  function refreshPopup() {
    if (!popupEl) return;
    popupEl.innerHTML = buildCalendar();
  }

  // ── GLOBAL HANDLERS (called from inline HTML) ────────────────
  window._calNav = function (dir) {
    currentMonth += dir;
    if (currentMonth > 11) {
      currentMonth = 0;
      currentYear++;
    }
    if (currentMonth < 0) {
      currentMonth = 11;
      currentYear--;
    }
    selectedDate = null;
    refreshPopup();
  };

  window._calSelectDay = function (day) {
    if (
      selectedDate &&
      selectedDate.day === day &&
      selectedDate.month === currentMonth
    ) {
      selectedDate = null;
    } else {
      selectedDate = { year: currentYear, month: currentMonth, day };
    }
    refreshPopup();
  };

  // ── INIT ─────────────────────────────────────────────────────
  // ── INIT ─────────────────────────────────────────────────────
  async function initCalendar() {
    await fetchReservations();
    await new Promise((resolve) => setTimeout(resolve, 200));

    const dateEl = document.getElementById("topbar-date");
    if (!dateEl) return;

    dateEl.style.cursor = "pointer";
    dateEl.style.userSelect = "none";
    dateEl.title = "Click to view calendar";

    const currentText = dateEl.textContent;
    dateEl.innerHTML = `
      <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right:6px;vertical-align:-1px;">
        <rect x="3" y="4" width="18" height="18" rx="2"/>
        <line x1="16" y1="2" x2="16" y2="6"/>
        <line x1="8" y1="2" x2="8" y2="6"/>
        <line x1="3" y1="10" x2="21" y2="10"/>
      </svg>${currentText}`;

    dateEl.addEventListener("click", function (e) {
      e.stopPropagation();
      openCalendar(dateEl);
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initCalendar);
  } else {
    initCalendar();
  }
})();
