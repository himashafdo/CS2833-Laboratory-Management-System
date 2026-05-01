/**
 * LabMS Notifications
 * Include in any page with a topbar bell icon (class="topbar-icon").
 * Usage: <script src="/notifications.js"></script>
 * Requires: token in sessionStorage, /api/reservations/my endpoint
 */

(function () {
  const API = "http://localhost:8080/api";

  // ── STYLES ──────────────────────────────────────────────────
  const style = document.createElement("style");
  style.textContent = `
    .notif-wrapper { position: relative; }

    .notif-badge {
      position: absolute; top: -2px; right: -2px;
      width: 16px; height: 16px; border-radius: 50%;
      background: #ef4444; border: 2px solid white;
      font-size: 9px; font-weight: 700; color: white;
      display: flex; align-items: center; justify-content: center;
      pointer-events: none;
    }

    .notif-popup {
      position: fixed;
      background: #ffffff;
      border: 1px solid #e2e8f0;
      border-radius: 18px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.15), 0 4px 20px rgba(0,0,0,0.08);
      width: 340px;
      z-index: 1000;
      overflow: hidden;
      animation: notifFadeIn 0.15s ease;
    }
    @keyframes notifFadeIn {
      from { opacity: 0; transform: translateY(-8px) scale(0.97); }
      to   { opacity: 1; transform: translateY(0) scale(1); }
    }

    .notif-header {
      padding: 16px 20px 12px;
      display: flex; align-items: center; justify-content: space-between;
      border-bottom: 1px solid #e2e8f0;
    }
    .notif-title {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 15px; font-weight: 700; color: #1a1a2e;
    }
    .notif-clear {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 12px; color: #f97316; font-weight: 600;
      cursor: pointer; background: none; border: none; padding: 0;
    }
    .notif-clear:hover { text-decoration: underline; }

    .notif-list {
      max-height: 320px; overflow-y: auto;
    }
    .notif-item {
      display: flex; align-items: flex-start; gap: 12px;
      padding: 14px 20px; border-bottom: 1px solid #f1f5f9;
      transition: background 0.15s; cursor: default;
    }
    .notif-item:last-child { border-bottom: none; }
    .notif-item:hover { background: #f8fafc; }
    .notif-item.unread { background: #fff7ed; }
    .notif-item.unread:hover { background: #ffedd5; }

    .notif-icon {
      width: 36px; height: 36px; border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
      flex-shrink: 0;
    }
    .notif-icon svg { width: 18px; height: 18px; }
    .notif-icon.pending  { background: #fef9c3; color: #854d0e; }
    .notif-icon.approved { background: #dcfce7; color: #15803d; }
    .notif-icon.cancelled{ background: #f1f5f9; color: #64748b; }
    .notif-icon.equipment{ background: #fff7ed; color: #f97316; }
    .notif-icon.lab      { background: #eff6ff; color: #3b82f6; }

    .notif-body { flex: 1; min-width: 0; }
    .notif-msg {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 13px; font-weight: 500; color: #1a1a2e;
      margin-bottom: 3px; line-height: 1.4;
    }
    .notif-msg span { font-weight: 700; }
    .notif-time {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 11px; color: #94a3b8;
    }

    .notif-dot {
      width: 7px; height: 7px; border-radius: 50%;
      background: #f97316; flex-shrink: 0; margin-top: 6px;
    }

    .notif-empty {
      padding: 40px 20px; text-align: center;
      font-family: 'Plus Jakarta Sans', sans-serif;
    }
    .notif-empty-icon { font-size: 32px; margin-bottom: 8px; }
    .notif-empty-text { font-size: 13px; color: #94a3b8; }

    .notif-footer {
      padding: 12px 20px; border-top: 1px solid #e2e8f0; text-align: center;
    }
    .notif-footer a {
      font-family: 'Plus Jakarta Sans', sans-serif;
      font-size: 13px; color: #f97316; font-weight: 600;
      text-decoration: none;
    }
    .notif-footer a:hover { text-decoration: underline; }

    .notif-overlay {
      position: fixed; inset: 0; z-index: 999; background: transparent;
    }
  `;
  document.head.appendChild(style);

  // ── STATE ────────────────────────────────────────────────────
  let reservations = [];
  let isOpen = false;
  let popupEl = null;
  let overlayEl = null;
  let seenIds = JSON.parse(sessionStorage.getItem("notif_seen") || "[]");

  // ── FETCH ────────────────────────────────────────────────────
  async function fetchReservations() {
    const token = sessionStorage.getItem("token");
    if (!token) return;
    try {
      const res = await fetch(`${API}/reservations/my`, {
        headers: { Authorization: "Bearer " + token },
      });
      reservations = await res.json();
      updateBadge();
    } catch (e) {
      reservations = [];
    }
  }

  // ── BUILD NOTIFICATIONS FROM RESERVATIONS ────────────────────
  function buildNotifications() {
    if (!reservations.length) return [];

    return reservations
      .filter((r) => r.status !== "CANCELLED")
      .slice(0, 10)
      .map((r) => {
        const name = r.equipment
          ? r.equipment.name
          : r.lab
            ? r.lab.labName
            : "Unknown";
        const type = r.equipment ? "equipment" : "lab";
        const start = new Date(r.startTime);
        const dateStr = start.toLocaleDateString("en-GB", {
          day: "numeric",
          month: "short",
        });
        const timeStr = start.toLocaleTimeString("en-GB", {
          hour: "2-digit",
          minute: "2-digit",
        });
        const isUnread = !seenIds.includes(r.id);

        let msg = "";
        let iconClass = "";
        let iconSvg = "";

        if (r.status === "PENDING") {
          msg = `<span>${name}</span> booking is pending approval`;
          iconClass = "pending";
          iconSvg = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>`;
        } else if (r.status === "APPROVED") {
          msg = `<span>${name}</span> booking confirmed for ${dateStr}`;
          iconClass = "approved";
          iconSvg = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>`;
        } else if (r.status === "COMPLETED") {
          msg = `<span>${name}</span> reservation completed on ${dateStr}`;
          iconClass = type;
          iconSvg = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>`;
        }

        return {
          id: r.id,
          msg,
          iconClass,
          iconSvg,
          time: `${dateStr} · ${timeStr}`,
          isUnread,
        };
      })
      .filter((n) => n.msg);
  }

  // ── BADGE ─────────────────────────────────────────────────────
  function updateBadge() {
    const notifs = buildNotifications();
    const unreadCount = notifs.filter((n) => n.isUnread).length;
    const badge = document.getElementById("notif-badge");
    if (badge) {
      badge.textContent = unreadCount > 9 ? "9+" : unreadCount;
      badge.style.display = unreadCount > 0 ? "flex" : "none";
    }
  }

  // ── RENDER POPUP ─────────────────────────────────────────────
  function renderPopup() {
    if (!popupEl) return;
    const notifs = buildNotifications();

    let listHTML = "";
    if (!notifs.length) {
      listHTML = `
        <div class="notif-empty">
          <div class="notif-empty-icon">🔔</div>
          <div class="notif-empty-text">No notifications yet</div>
        </div>`;
    } else {
      listHTML = notifs
        .map(
          (n) => `
        <div class="notif-item ${n.isUnread ? "unread" : ""}">
          <div class="notif-icon ${n.iconClass}">${n.iconSvg}</div>
          <div class="notif-body">
            <div class="notif-msg">${n.msg}</div>
            <div class="notif-time">${n.time}</div>
          </div>
          ${n.isUnread ? '<div class="notif-dot"></div>' : ""}
        </div>`,
        )
        .join("");
    }

    popupEl.innerHTML = `
      <div class="notif-header">
        <div class="notif-title">Notifications</div>
        <button class="notif-clear" onclick="window._notifMarkAllRead()">Mark all read</button>
      </div>
      <div class="notif-list">${listHTML}</div>
      ${
        notifs.length > 0
          ? `
        <div class="notif-footer">
          <a href="/my-reservations.html">View all reservations →</a>
        </div>`
          : ""
      }`;
  }

  // ── OPEN / CLOSE ─────────────────────────────────────────────
  function openNotif(anchorEl) {
    if (isOpen) {
      closeNotif();
      return;
    }
    isOpen = true;

    overlayEl = document.createElement("div");
    overlayEl.className = "notif-overlay";
    overlayEl.addEventListener("click", closeNotif);
    document.body.appendChild(overlayEl);

    popupEl = document.createElement("div");
    popupEl.className = "notif-popup";
    popupEl.addEventListener("click", (e) => e.stopPropagation());
    document.body.appendChild(popupEl);

    const rect = anchorEl.getBoundingClientRect();
    popupEl.style.top = rect.bottom + 8 + "px";
    popupEl.style.right = window.innerWidth - rect.right + "px";

    renderPopup();
  }

  function closeNotif() {
    if (!isOpen) return;
    isOpen = false;
    if (popupEl) {
      popupEl.remove();
      popupEl = null;
    }
    if (overlayEl) {
      overlayEl.remove();
      overlayEl = null;
    }
  }

  // ── GLOBAL ───────────────────────────────────────────────────
  window._notifMarkAllRead = function () {
    const notifs = buildNotifications();
    seenIds = [...new Set([...seenIds, ...notifs.map((n) => n.id)])];
    sessionStorage.setItem("notif_seen", JSON.stringify(seenIds));
    updateBadge();
    renderPopup();
  };

  // ── INIT ─────────────────────────────────────────────────────
  async function initNotifications() {
    await fetchReservations();
    await new Promise((resolve) => setTimeout(resolve, 200));

    // Find bell icon
    const bellEl = document.querySelector(".topbar-icon");
    if (!bellEl) return;

    // Wrap in relative container and add badge
    bellEl.style.position = "relative";
    const badge = document.createElement("div");
    badge.className = "notif-badge";
    badge.id = "notif-badge";
    badge.style.display = "none";
    bellEl.appendChild(badge);

    updateBadge();

    bellEl.addEventListener("click", function (e) {
      e.stopPropagation();
      openNotif(bellEl);
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initNotifications);
  } else {
    initNotifications();
  }
})();
