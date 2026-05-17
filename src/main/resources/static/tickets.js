/**
 * Lab Management System — Admin Ticket Management Logic
 * Tailored directly for admin-tickets.html
 */

// ── CONFIGURATION & SESSION STATE ──
const API = "http://localhost:8800/api";
const token = sessionStorage.getItem("token");
const username = sessionStorage.getItem("username");

// ── ROUTE GUARD (AUTH CHECK) ──
if (!token) {
  window.location.href = "/login.html";
}

// Global cache for real-time search filtering
let allTickets = [];

// Initialize everything when the DOM is fully parsed
document.addEventListener("DOMContentLoaded", () => {
  initializeUIHeader();
  setupEventListeners();
  loadTickets();
});

/**
 * Populates header text, system date, and dynamic user avatar initials
 */
function initializeUIHeader() {
  // Sync user session details
  if (username) {
    const initials = username.substring(0, 2).toUpperCase();
    
    const sidebarAvatar = document.getElementById("sidebar-avatar");
    const topbarAvatar = document.getElementById("topbar-avatar");
    const sidebarName = document.getElementById("sidebar-name");

    if (sidebarAvatar) sidebarAvatar.textContent = initials;
    if (topbarAvatar) topbarAvatar.textContent = initials;
    if (sidebarName) sidebarName.textContent = username;
  }

  // Sync formatted localized date string
  const topbarDate = document.getElementById("topbar-date");
  if (topbarDate) {
    topbarDate.textContent = new Date().toLocaleDateString("en-GB", {
      weekday: "short",
      day: "numeric",
      month: "long",
      year: "numeric"
    });
  }
}

/**
 * Binds DOM input listeners for user interactions
 */
function setupEventListeners() {
  const searchInput = document.getElementById('searchInput');
  if (searchInput) {
    searchInput.addEventListener('input', handleSearchFilter);
  }
}

/**
 * Asynchronously fetches all admin tickets from the core backend API
 */
async function loadTickets() {
  const container = document.getElementById("tickets-container");
  
  try {
    const res = await fetch(`${API}/admin/tickets`, {
      method: "GET",
      headers: { 
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });
    
    if (!res.ok) throw new Error("Failed to fetch tickets from the endpoint");
    
    allTickets = await res.json();
    
    updateStats(allTickets);
    renderTickets(allTickets);
  } catch (error) {
    console.error("Critical System Data Fetch Fault:", error);
    if (container) {
      container.innerHTML = `<div class="empty-state">Failed to load tickets. Please ensure backend is running.</div>`;
    }
    // Fallback counter updates to zero out the loading indicators
    resetCountersToZero();
  }
}

/**
 * Processes, filters, and dynamically updates the upper statistics metrics counters
 */
function updateStats(tickets) {
  const total = tickets.length;
  
  // The original issue status drives the open/progress/resolved state logic
  const open = tickets.filter(t => t.originalIssue && t.originalIssue.status === 'OPEN').length;
  const progress = tickets.filter(t => t.originalIssue && t.originalIssue.status === 'IN_PROGRESS').length;
  const resolved = tickets.filter(t => t.originalIssue && (t.originalIssue.status === 'RESOLVED' || t.originalIssue.status === 'CLOSED')).length;

  updateDOMText("stat-total", total);
  updateDOMText("stat-open", open);
  updateDOMText("stat-progress", progress);
  updateDOMText("stat-resolved", resolved);
}

/**
 * Generates and appends premium UI data grid cards into the core wrapper container
 */
function renderTickets(tickets) {
  const container = document.getElementById("tickets-container");
  if (!container) return;
  
  if (tickets.length === 0) {
    container.innerHTML = `<div class="empty-state">No tickets found in the system.</div>`;
    return;
  }

  // Sort chronologically by assignment date, newest items first
  tickets.sort((a, b) => new Date(b.assignmentDate || 0) - new Date(a.assignmentDate || 0));

  container.innerHTML = tickets.map(ticket => {
    const issue = ticket.originalIssue || {};
    const status = issue.status || 'OPEN';
    const priority = ticket.adminPriority || 'MEDIUM';
    const date = new Date(ticket.assignmentDate || new Date()).toLocaleDateString("en-GB", { 
      day: 'numeric', 
      month: 'short', 
      year: 'numeric' 
    });
    
    const reporter = issue.user ? issue.user.username : 'Student';
    const labName = issue.lab ? issue.lab.labName : (issue.equipment ? 'Equipment Issue' : 'General');
    
    const statusClass = "status-" + status.toLowerCase();
    const priorityClass = "priority-" + priority.toLowerCase();
    const statusLabel = status.replace("_", " ");

    return `
      <div class="ticket-card">
        <div class="ticket-id">TCK-${String(ticket.id).padStart(3, '0')}</div>
        <div class="ticket-info">
          <div class="ticket-title">${escapeHTML(issue.title || 'Untitled Issue')}</div>
          <div class="ticket-meta">
            <span>Reported by <strong>${escapeHTML(reporter)}</strong></span>
            <div class="meta-divider"></div>
            <span>${escapeHTML(labName)}</span>
            <div class="meta-divider"></div>
            <span>Assigned ${date}</span>
          </div>
        </div>
        <div class="ticket-badges">
          <span class="badge ${statusClass}">${statusLabel}</span>
          <span class="badge ${priorityClass}">${priority}</span>
        </div>
        <div class="ticket-action">
          <button class="btn-view" onclick="viewTicket(${ticket.id})">Review</button>
        </div>
      </div>
    `;
  }).join("");
}

/**
 * Real-time event match query sequence calculations across systemic data states
 */
function handleSearchFilter(e) {
  const term = e.target.value.toLowerCase();
  const filtered = allTickets.filter(t => {
    const title = (t.originalIssue?.title || "").toLowerCase();
    const id = String(t.id).toLowerCase();
    const reporter = (t.originalIssue?.user?.username || "").toLowerCase();
    
    return title.includes(term) || id.includes(term) || reporter.includes(term);
  });
  renderTickets(filtered);
}

/**
 * Trigger review pipeline actions (Modal configurations, status changes, assignments)
 */
function viewTicket(id) {
  // Can be seamlessly updated to route overlay wrappers or multi-state panels
  alert('Opening resolution modal for Ticket: TCK-' + String(id).padStart(3, '0'));
}

// ── UTILITY REFACTOR PATTERNS ──
function updateDOMText(elementId, value) {
  const element = document.getElementById(elementId);
  if (element) element.textContent = value;
}

function resetCountersToZero() {
  ["stat-total", "stat-open", "stat-progress", "stat-resolved"].forEach(id => updateDOMText(id, "0"));
}

function escapeHTML(str) {
  if (!str) return '';
  return str.replace(/[&<>'"]/g, 
    tag => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[tag] || tag)
  );
}