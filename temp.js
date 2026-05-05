
        const API_BASE = 'http://localhost:8080/api/requests';
        let currentUserRole = '';
        let myChartInstance = null;
        let pieChartInstance = null;

        // Initialize Page
        document.addEventListener('DOMContentLoaded', () => {
            const token = sessionStorage.getItem('token');
            if (!token) {
                window.location.href = '/index.html';
                return;
            }

            // Set today's date
            const dateOptions = { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric' };
            document.getElementById('topbar-date').textContent = new Date().toLocaleDateString('en-US', dateOptions);

            // Extract role from JWT token
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                currentUserRole = payload.role;
                const username = payload.sub || sessionStorage.getItem('username') || 'User';

                document.body.setAttribute('data-role', currentUserRole);
                document.getElementById('sidebar-name').textContent = username;
                document.getElementById('sidebar-role').textContent = currentUserRole.replace('_', ' ');
                const initial = username.charAt(0).toUpperCase();
                document.getElementById('sidebar-avatar').textContent = initial;
                document.getElementById('topbar-avatar').textContent = initial;

                loadRequestsBasedOnRole();
                if (currentUserRole !== 'STUDENT') {
                    loadDashboardStats();
                }
            } catch (e) {
                console.error("Invalid token format", e);
                logout();
            }
        });

        // Setup API headers
        function getHeaders() {
            return {
                'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            };
        }

        function loadRequestsBasedOnRole() {
            if (currentUserRole === 'STUDENT') {
                loadMyRequests();
            } else {
                loadAllRequests();
                loadDashboardStats();
            }
        }

        // --- Fetch Data ---
        async function loadMyRequests() {
            try {
                const res = await fetch(`${API_BASE}/my`, { headers: getHeaders() });
                if (!res.ok) throw new Error('Failed to load requests');
                const data = await res.json();
                renderTable(data);
            } catch (err) {
                console.error(err);
                document.getElementById('requests-tbody').innerHTML = `<tr><td colspan="7" class="empty-state" style="color:var(--danger)">Error loading requests.</td></tr>`;
            }
        }

        async function loadAllRequests() {
            try {
                const res = await fetch(API_BASE, { headers: getHeaders() });
                if (!res.ok) throw new Error('Failed to load requests');
                const data = await res.json();
                renderTable(data);
            } catch (err) {
                console.error(err);
                document.getElementById('requests-tbody').innerHTML = `<tr><td colspan="7" class="empty-state" style="color:var(--danger)">Error loading requests.</td></tr>`;
            }
        }

        async function loadDashboardStats() {
            try {
                const res = await fetch(`${API_BASE}/stats`, { headers: getHeaders() });
                if (!res.ok) return;
                const data = await res.json();

                document.getElementById('stat-total').textContent = data.total;
                document.getElementById('stat-pending').textContent = data.pending;
                document.getElementById('stat-approved').textContent = data.approved;
                document.getElementById('stat-rejected').textContent = data.rejected;

                renderCharts(data);
            } catch (err) { console.error("Error loading stats", err); }
        }

        // --- Render UI ---
        function renderTable(requests) {
            const tbody = document.getElementById('requests-tbody');
            if (requests.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" class="empty-state">No equipment requests found.</td></tr>`;
                return;
            }

            tbody.innerHTML = '';
            requests.forEach(req => {
                const date = new Date(req.createdAt).toLocaleDateString();
                const tr = document.createElement('tr');

                let actionsHtml = req.adminNote ? `<div style="font-size:11px; color:var(--text-muted); margin-top:4px">Note: ${req.adminNote}</div>` : '';

                if (currentUserRole !== 'STUDENT' && req.status === 'PENDING') {
                    actionsHtml = `
                        <div class="action-btns">
                            <button class="btn btn-success btn-small" onclick="openAdminModal(${req.id}, 'approve')">Approve</button>
                            <button class="btn btn-danger btn-small" onclick="openAdminModal(${req.id}, 'reject')">Reject</button>
                        </div>
                    `;
                } else if (currentUserRole === 'STUDENT' && req.status === 'PENDING') {
                    actionsHtml = `
                        <div class="action-btns">
                            <button class="btn btn-outline btn-small" onclick="openEditModal(${req.id}, '${req.itemName.replace(/'/g, "\\'")}', ${req.quantity}, '${req.urgencyLevel}', '${(req.description || '').replace(/'/g, "\\'")}')" style="font-size:11px; padding:4px 10px;">?????? Edit</button>
                            <button class="btn btn-danger btn-small" onclick="openDeleteModal(${req.id})" style="font-size:11px; padding:4px 10px;">??????? Delete</button>
                        </div>
                    `;
                } else if (currentUserRole === 'STUDENT') {
                    actionsHtml = '<span style="color:var(--text3); font-size:12px;">-</span>';
                }

                let rowHtml = `
                    <td>
                        <div style="font-weight:600">${req.itemName}</div>
                        <div style="font-size:12px; color:var(--text-muted); max-width:250px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;" title="${req.description}">${req.description}</div>
                    </td>
                    <td>${req.quantity}</td>
                    <td><span class="badge ${req.urgencyLevel.toLowerCase()}">${req.urgencyLevel}</span></td>
                `;

                if (currentUserRole !== 'STUDENT') {
                    rowHtml += `<td>${req.submittedBy}</td>`;
                }

                rowHtml += `
                    <td>${date}</td>
                    <td><span class="badge ${req.status.toLowerCase()}">${req.status}</span></td>
                    <td>${actionsHtml}</td>
                `;

                tr.innerHTML = rowHtml;
                tbody.appendChild(tr);
            });
        }

        function renderCharts(data) {
            // Bar Chart (Monthly)
            const ctxBar = document.getElementById('monthlyChart').getContext('2d');
            const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
            const monthData = months.map((_, i) => data.monthly[i + 1] || 0);

            if (myChartInstance) myChartInstance.destroy();
            myChartInstance = new Chart(ctxBar, {
                type: 'bar',
                data: {
                    labels: months,
                    datasets: [{
                        label: 'Requests',
                        data: monthData,
                        backgroundColor: '#f97316',
                        borderRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { display: false } },
                    scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                }
            });

            // Pie Chart (Status)
            const ctxPie = document.getElementById('statusChart').getContext('2d');
            if (pieChartInstance) pieChartInstance.destroy();
            pieChartInstance = new Chart(ctxPie, {
                type: 'doughnut',
                data: {
                    labels: ['Approved', 'Rejected'],
                    datasets: [{
                        data: [data.approved, data.rejected],
                        backgroundColor: ['#10b981', '#ef4444'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    cutout: '70%',
                    plugins: {
                        legend: { position: 'bottom' }
                    }
                }
            });
        }

        // --- Student Actions ---
        function openRequestModal() {
            document.getElementById('requestForm').reset();
            document.getElementById('requestModal').classList.add('active');
        }

        async function submitRequest(e) {
            e.preventDefault();
            const payload = {
                itemName: document.getElementById('req-item').value,
                quantity: parseInt(document.getElementById('req-qty').value),
                urgencyLevel: document.getElementById('req-urgency').value,
                description: document.getElementById('req-desc').value
            };

            try {
                const res = await fetch(API_BASE, {
                    method: 'POST',
                    headers: getHeaders(),
                    body: JSON.stringify(payload)
                });

                if (res.ok) {
                    closeModal('requestModal');
                    loadMyRequests();
                } else {
                    const err = await res.json();
                    alert("Error: " + err.error);
                }
            } catch (error) {
                console.error("Submission failed", error);
                alert("Submission failed. Ensure backend is running.");
            }
        }

        // --- Admin Actions ---
        function openAdminModal(id, action) {
            document.getElementById('admin-req-id').value = id;
            document.getElementById('admin-action-type').value = action;
            document.getElementById('admin-note').value = '';
            document.getElementById('adminModalTitle').textContent = action === 'approve' ? 'Approve Request' : 'Reject Request';

            const btn = document.getElementById('admin-confirm-btn');
            btn.className = action === 'approve' ? 'btn btn-success' : 'btn btn-danger';

            document.getElementById('adminModal').classList.add('active');
        }

        async function confirmAdminAction() {
            const id = document.getElementById('admin-req-id').value;
            const action = document.getElementById('admin-action-type').value;
            const note = document.getElementById('admin-note').value;

            try {
                const res = await fetch(`${API_BASE}/${id}/${action}`, {
                    method: 'PUT',
                    headers: getHeaders(),
                    body: JSON.stringify({ adminNote: note })
                });

                if (res.ok) {
                    closeModal('adminModal');
                    loadAllRequests();
                    loadDashboardStats();
                } else {
                    const err = await res.json();
                    alert("Error: " + err.error);
                }
            } catch (error) {
                console.error("Action failed", error);
            }
        }

        // --- Utils ---
        function closeModal(modalId) {
            document.getElementById(modalId).classList.remove('active');
        }

        function logout() {
            sessionStorage.clear();
            window.location.href = '/index.html';
        }
    
