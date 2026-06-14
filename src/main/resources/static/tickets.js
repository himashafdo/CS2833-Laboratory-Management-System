/**
 * Lab Management System — Admin Ticket Management Logic
 * Tailored directly for admin-tickets.html
 */

// ── CONFIGURATION & SESSION STATE ──
const API = "http://localhost:8800/api"; // Ensure this port matches your Spring Boot application properties
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

// ── MODAL CONTROL LOGIC ──

function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.style.display = 'flex';
}

function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) modal.style.display = 'none';
}

// ── CORE DATA OPERATIONS (API INTEGRATION) ──

/**
 * Asynchronously fetches all admin tickets from the core backend API (GET)
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
 * Assigns an issue to a staff member, creating a new ticket (POST)
 */
async function submitAssignment(e) {
  e.preventDefault();
  
  const payload = {
    issueId: document.getElementById('assignIssueId').value,
    staffId: document.getElementById('assignStaffId').value,
    priority: document.getElementById('assignPriority').value
  };

  try {
    const res = await fetch(`${API}/admin/tickets/assign`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errData = await res.json();
      throw new Error(errData.error || "Failed to assign ticket");
    }
    
    alert("Ticket assigned successfully!");
    closeModal('assignModal');
    document.getElementById('assignForm').reset();
    
    // Refresh List to show newly assigned ticket
    loadTickets(); 
  } catch (err) {
    alert("Error assigning ticket: " + err.message);
  }
}

/**
 * Resolves an active ticket with details and internal notes (PATCH)
 */
async function submitResolution(e) {
  e.preventDefault();
  
  const ticketId = document.getElementById('resolveTicketId').value;
  const payload = {
    resolution: document.getElementById('resolveDetails').value,
    notes: document.getElementById('resolveNotes').value
  };

  try {
    const res = await fetch(`${API}/admin/tickets/${ticketId}/resolve`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) {
      const errData = await res.json();
      throw new Error(errData.error || "Failed to resolve ticket");
    }
    
    alert("Ticket marked as resolved!");
    closeModal('resolveModal');
    document.getElementById('resolveForm').reset();
    
    // Refresh List to update UI badges and stats
    loadTickets(); 
  } catch (err) {
    alert("Error resolving ticket: " + err.message);
  }
}

/**
 * Trigger review pipeline actions (Populates resolution modal based on Ticket ID)
 */
function viewTicket(id) {
  const resolveTicketIdInput = document.getElementById('resolveTicketId');
  const resolveTicketIdDisplay = document.getElementById('resolveTicketIdDisplay');
  const resolveDetailsInput = document.getElementById('resolveDetails');
  const resolveNotesInput = document.getElementById('resolveNotes');

  if (resolveTicketIdInput && resolveTicketIdDisplay) {
    resolveTicketIdInput.value = id;
    resolveTicketIdDisplay.textContent = 'TCK-' + String(id).padStart(3, '0');
    resolveDetailsInput.value = '';
    resolveNotesInput.value = '';
    
    openModal('resolveModal');
  } else {
    console.error("Resolution modal elements are missing from the DOM.");
  }
}

// ── UI RENDERING & DATA MANIPULATION ──

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

    // Ensure users cannot re-resolve an already resolved ticket
    const actionButton = status === 'RESOLVED' || status === 'CLOSED'
      ? `<button class="btn-view" style="background:#cbd5e1; cursor:not-allowed;" disabled>Resolved</button>`
      : `<button class="btn-view" onclick="viewTicket(${ticket.id})">Review</button>`;

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
          ${actionButton}
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