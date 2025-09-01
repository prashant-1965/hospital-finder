// adminDashboard.js

class AdminDashboard {
    constructor() {
        this.currentSection = 'overview';
        this.currentModal = null;
        this.pendingRequests = {
            doctors: [],
            facilities: [],
            hospitals: []
        };
        this.countriesLoaded = false; // Flag to prevent duplicate calls
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadInitialData();
        this.showSection('overview');
    }

    setupEventListeners() {
        // Navigation links
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const section = e.currentTarget.getAttribute('data-section');
                this.showSection(section);
            });
        });

        // Form submissions
        document.getElementById('country-form').addEventListener('submit', (e) => {
            this.handleCountrySubmission(e);
        });

        document.getElementById('state-form').addEventListener('submit', (e) => {
            this.handleStateSubmission(e);
        });

        // Country dropdown click handler - load countries when dropdown is clicked
        document.getElementById('stateCountryName').addEventListener('click', () => {
            if (!this.countriesLoaded) {
                this.loadCountries();
            }
        });

        // Modal close handlers
        document.getElementById('modal-overlay').addEventListener('click', (e) => {
            if (e.target.id === 'modal-overlay') {
                this.closeModal();
            }
        });

        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.closeModal();
            }
        });
    }

    showSection(sectionName) {
        // Update navigation
        document.querySelectorAll('.nav-link').forEach(link => {
            link.classList.remove('active');
        });
        document.querySelector(`[data-section="${sectionName}"]`).classList.add('active');

        // Update content sections
        document.querySelectorAll('.content-section').forEach(section => {
            section.classList.remove('active');
        });
        document.getElementById(sectionName).classList.add('active');

        this.currentSection = sectionName;

        // Load section-specific data
        switch(sectionName) {
            case 'doctor-register':
                this.loadDoctorRequests();
                break;
            case 'facility-register':
                this.loadFacilityRequests();
                break;
            case 'hospital-register':
                this.loadHospitalRequests();
                break;
            case 'state-register':
                // Don't auto-load countries here, only when dropdown is clicked
                break;
            case 'overview':
                this.updateOverviewStats();
                break;
        }
    }

    async loadInitialData() {
        try {
            // Load all initial data for overview (excluding countries as they load on demand)
            await Promise.all([
                this.loadDoctorRequests(),
                this.loadFacilityRequests(),
                this.loadHospitalRequests()
            ]);
            this.updateOverviewStats();
        } catch (error) {
            console.error('Error loading initial data:', error);
            this.showToast('Error loading dashboard data', 'error');
        }
    }

    async loadDoctorRequests() {
        this.showLoading(true);
        try {
            // Replace with actual API endpoint to get pending doctor requests
            // const response = await fetch('/api/admin/doctor-requests');
            // const requests = await response.json();

            // Mock data for demonstration
            const requests = [
                {
                    id: 1,
                    doctorName: 'Dr. John Smith',
                    specialization: 'Cardiology',
                    experience: '10 years',
                    qualification: 'MBBS, MD',
                    email: 'john.smith@email.com',
                    phone: '+1234567890',
                    requestDate: '2024-01-15'
                },
                {
                    id: 2,
                    doctorName: 'Dr. Sarah Johnson',
                    specialization: 'Neurology',
                    experience: '8 years',
                    qualification: 'MBBS, DM',
                    email: 'sarah.johnson@email.com',
                    phone: '+1234567891',
                    requestDate: '2024-01-16'
                },
                {
                    id: 3,
                    doctorName: 'Dr. Michael Brown',
                    specialization: 'Orthopedics',
                    experience: '12 years',
                    qualification: 'MBBS, MS',
                    email: 'michael.brown@email.com',
                    phone: '+1234567892',
                    requestDate: '2024-01-17'
                }
            ];

            this.pendingRequests.doctors = requests;
            this.renderDoctorRequests(requests);
        } catch (error) {
            console.error('Error loading doctor requests:', error);
            this.showToast('Failed to load doctor requests', 'error');
            this.renderEmptyState('doctor-requests', 'No doctor requests found');
        } finally {
            this.showLoading(false);
        }
    }

    async loadFacilityRequests() {
        this.showLoading(true);
        try {
            // Replace with actual API endpoint to get pending facility requests
            // const response = await fetch('/api/admin/facility-requests');
            // const requests = await response.json();

            // Mock data for demonstration
            const requests = [
                {
                    id: 1,
                    facilityName: 'Advanced MRI Center',
                    facilityDescription: 'State-of-the-art MRI facility with 3T scanner for detailed imaging',
                    requestDate: '2024-01-17'
                },
                {
                    id: 2,
                    facilityName: 'Emergency Care Unit',
                    facilityDescription: '24/7 emergency medical services with trauma care capabilities',
                    requestDate: '2024-01-18'
                },
                {
                    id: 3,
                    facilityName: 'Cardiac Surgery Suite',
                    facilityDescription: 'Specialized operating room for cardiovascular procedures',
                    requestDate: '2024-01-19'
                }
            ];

            this.pendingRequests.facilities = requests;
            this.renderFacilityRequests(requests);
        } catch (error) {
            console.error('Error loading facility requests:', error);
            this.showToast('Failed to load facility requests', 'error');
            this.renderEmptyState('facility-requests', 'No facility requests found');
        } finally {
            this.showLoading(false);
        }
    }

    async loadHospitalRequests() {
        this.showLoading(true);
        try {
            // Replace with actual API endpoint to get pending hospital requests
            // const response = await fetch('/api/admin/hospital-requests');
            // const requests = await response.json();

            // Mock data for demonstration
            const requests = [
                {
                    id: 1,
                    hospitalName: 'City General Hospital',
                    hospitalType: 'Public',
                    hospitalYearOfEstablishment: 1995,
                    hospitalNumOfUsersServed: 50000,
                    hospitalContact: '+1234567892',
                    hospitalAddress: '123 Main Street, City Center',
                    countryName: 'USA',
                    stateName: 'California',
                    facilities: ['Emergency Care', 'ICU', 'Surgery', 'Radiology'],
                    requestDate: '2024-01-19'
                },
                {
                    id: 2,
                    hospitalName: 'Metro Children Hospital',
                    hospitalType: 'Private',
                    hospitalYearOfEstablishment: 2005,
                    hospitalNumOfUsersServed: 25000,
                    hospitalContact: '+1234567893',
                    hospitalAddress: '456 Park Avenue, Downtown',
                    countryName: 'USA',
                    stateName: 'New York',
                    facilities: ['Pediatrics', 'NICU', 'Pediatric Surgery'],
                    requestDate: '2024-01-20'
                }
            ];

            this.pendingRequests.hospitals = requests;
            this.renderHospitalRequests(requests);
        } catch (error) {
            console.error('Error loading hospital requests:', error);
            this.showToast('Failed to load hospital requests', 'error');
            this.renderEmptyState('hospital-requests', 'No hospital requests found');
        } finally {
            this.showLoading(false);
        }
    }

    async loadCountries() {
        // Prevent duplicate calls
        if (this.countriesLoaded) {
            return;
        }

        try {
            const response = await fetch('/country/getCountryList');

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const countries = await response.json();

            const countrySelect = document.getElementById('stateCountryName');
            countrySelect.innerHTML = '<option value="">Select Country</option>';

            // Handle the CountryListProjection structure
            countries.forEach(countryObj => {
                const option = document.createElement('option');
                option.value = countryObj.countryName;
                option.textContent = countryObj.countryName;
                countrySelect.appendChild(option);
            });

            // Mark as loaded to prevent future duplicate calls
            this.countriesLoaded = true;
            console.log('Countries loaded successfully:', countries);
        } catch (error) {
            console.error('Error loading countries:', error);
            this.showToast('Failed to load countries from server', 'error');

            // Fallback to empty dropdown on error
            const countrySelect = document.getElementById('stateCountryName');
            countrySelect.innerHTML = '<option value="">Failed to load countries</option>';
        }
    }

    renderDoctorRequests(requests) {
        const container = document.getElementById('doctor-requests');

        if (requests.length === 0) {
            this.renderEmptyState('doctor-requests', 'No pending doctor requests');
            return;
        }

        container.innerHTML = requests.map(request => `
            <div class="request-card" data-request-id="${request.id}" data-type="doctor">
                <div class="request-header">
                    <i class="fas fa-user-md"></i> ${request.doctorName}
                </div>
                <div class="request-body">
                    <div class="request-detail">
                        <span class="detail-label">Specialization:</span>
                        <span class="detail-value">${request.specialization}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Experience:</span>
                        <span class="detail-value">${request.experience}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Qualification:</span>
                        <span class="detail-value">${request.qualification}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Email:</span>
                        <span class="detail-value">${request.email}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Request Date:</span>
                        <span class="detail-value">${request.requestDate}</span>
                    </div>
                </div>
                <div class="request-actions">
                    <button class="btn btn-info" onclick="adminDashboard.showRequestDetails('${request.id}', 'doctor')">
                        <i class="fas fa-eye"></i> View Details
                    </button>
                    <button class="btn btn-success" onclick="adminDashboard.approveRequest('${request.id}', 'doctor')">
                        <i class="fas fa-check"></i> Approve
                    </button>
                    <button class="btn btn-danger" onclick="adminDashboard.rejectRequest('${request.id}', 'doctor')">
                        <i class="fas fa-times"></i> Reject
                    </button>
                </div>
            </div>
        `).join('');
    }

    renderFacilityRequests(requests) {
        const container = document.getElementById('facility-requests');

        if (requests.length === 0) {
            this.renderEmptyState('facility-requests', 'No pending facility requests');
            return;
        }

        container.innerHTML = requests.map(request => `
            <div class="request-card" data-request-id="${request.id}" data-type="facility">
                <div class="request-header">
                    <i class="fas fa-hospital"></i> ${request.facilityName}
                </div>
                <div class="request-body">
                    <div class="request-detail">
                        <span class="detail-label">Description:</span>
                        <span class="detail-value">${request.facilityDescription}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Request Date:</span>
                        <span class="detail-value">${request.requestDate}</span>
                    </div>
                </div>
                <div class="request-actions">
                    <button class="btn btn-info" onclick="adminDashboard.showRequestDetails('${request.id}', 'facility')">
                        <i class="fas fa-eye"></i> View Details
                    </button>
                    <button class="btn btn-success" onclick="adminDashboard.approveRequest('${request.id}', 'facility')">
                        <i class="fas fa-check"></i> Approve
                    </button>
                    <button class="btn btn-danger" onclick="adminDashboard.rejectRequest('${request.id}', 'facility')">
                        <i class="fas fa-times"></i> Reject
                    </button>
                </div>
            </div>
        `).join('');
    }

    renderHospitalRequests(requests) {
        const container = document.getElementById('hospital-requests');

        if (requests.length === 0) {
            this.renderEmptyState('hospital-requests', 'No pending hospital requests');
            return;
        }

        container.innerHTML = requests.map(request => `
            <div class="request-card" data-request-id="${request.id}" data-type="hospital">
                <div class="request-header">
                    <i class="fas fa-building"></i> ${request.hospitalName}
                </div>
                <div class="request-body">
                    <div class="request-detail">
                        <span class="detail-label">Type:</span>
                        <span class="detail-value">${request.hospitalType}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Established:</span>
                        <span class="detail-value">${request.hospitalYearOfEstablishment}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Users Served:</span>
                        <span class="detail-value">${request.hospitalNumOfUsersServed.toLocaleString()}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Location:</span>
                        <span class="detail-value">${request.stateName}, ${request.countryName}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Facilities:</span>
                        <span class="detail-value">${request.facilities.join(', ')}</span>
                    </div>
                </div>
                <div class="request-actions">
                    <button class="btn btn-info" onclick="adminDashboard.showRequestDetails('${request.id}', 'hospital')">
                        <i class="fas fa-eye"></i> View Details
                    </button>
                    <button class="btn btn-success" onclick="adminDashboard.approveRequest('${request.id}', 'hospital')">
                        <i class="fas fa-check"></i> Approve
                    </button>
                    <button class="btn btn-danger" onclick="adminDashboard.rejectRequest('${request.id}', 'hospital')">
                        <i class="fas fa-times"></i> Reject
                    </button>
                </div>
            </div>
        `).join('');
    }

    renderEmptyState(containerId, message) {
        const container = document.getElementById(containerId);
        container.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-inbox"></i>
                <h3>No Data Found</h3>
                <p>${message}</p>
            </div>
        `;
    }

    showRequestDetails(requestId, type) {
        const request = this.findRequest(requestId, type);
        if (!request) {
            this.showToast('Request not found', 'error');
            return;
        }

        const modal = document.getElementById('modal-overlay');
        const title = document.getElementById('modal-title');
        const content = document.getElementById('modal-content');

        title.textContent = `${type.charAt(0).toUpperCase() + type.slice(1)} Request Details`;

        let detailsHTML = '';
        switch(type) {
            case 'doctor':
                detailsHTML = `
                    <div class="request-detail">
                        <span class="detail-label">Name:</span>
                        <span class="detail-value">${request.doctorName}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Specialization:</span>
                        <span class="detail-value">${request.specialization}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Experience:</span>
                        <span class="detail-value">${request.experience}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Qualification:</span>
                        <span class="detail-value">${request.qualification}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Email:</span>
                        <span class="detail-value">${request.email}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Phone:</span>
                        <span class="detail-value">${request.phone}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Request Date:</span>
                        <span class="detail-value">${request.requestDate}</span>
                    </div>
                `;
                break;
            case 'facility':
                detailsHTML = `
                    <div class="request-detail">
                        <span class="detail-label">Facility Name:</span>
                        <span class="detail-value">${request.facilityName}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Description:</span>
                        <span class="detail-value">${request.facilityDescription}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Request Date:</span>
                        <span class="detail-value">${request.requestDate}</span>
                    </div>
                `;
                break;
            case 'hospital':
                detailsHTML = `
                    <div class="request-detail">
                        <span class="detail-label">Hospital Name:</span>
                        <span class="detail-value">${request.hospitalName}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Type:</span>
                        <span class="detail-value">${request.hospitalType}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Year of Establishment:</span>
                        <span class="detail-value">${request.hospitalYearOfEstablishment}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Users Served:</span>
                        <span class="detail-value">${request.hospitalNumOfUsersServed.toLocaleString()}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Contact:</span>
                        <span class="detail-value">${request.hospitalContact}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Address:</span>
                        <span class="detail-value">${request.hospitalAddress}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Location:</span>
                        <span class="detail-value">${request.stateName}, ${request.countryName}</span>
                    </div>
                    <div class="request-detail">
                        <span class="detail-label">Facilities:</span>
                        <span class="detail-value">${request.facilities.join(', ')}</span>
                    </div>
                `;
                break;
        }

        content.innerHTML = detailsHTML;

        // Set up modal action buttons
        const approveBtn = document.getElementById('approve-btn');
        const rejectBtn = document.getElementById('reject-btn');

        approveBtn.onclick = () => {
            this.approveRequest(requestId, type);
            this.closeModal();
        };

        rejectBtn.onclick = () => {
            this.rejectRequest(requestId, type);
            this.closeModal();
        };

        this.currentModal = { requestId, type };
        modal.classList.add('show');
    }

    closeModal() {
        const modal = document.getElementById('modal-overlay');
        modal.classList.remove('show');
        this.currentModal = null;
    }

    findRequest(requestId, type) {
        const requests = this.pendingRequests[type + 's'] || [];
        return requests.find(req => req.id.toString() === requestId.toString());
    }

    async approveRequest(requestId, type) {
        const request = this.findRequest(requestId, type);
        if (!request) {
            this.showToast('Request not found', 'error');
            return;
        }

        this.showLoading(true);

        try {
            let endpoint = '';
            let payload = {};

            switch(type) {
                case 'doctor':
                    endpoint = '/doctor/register';
                    payload = {
                        doctorName: request.doctorName,
                        specialization: request.specialization,
                        experience: request.experience,
                        qualification: request.qualification,
                        email: request.email,
                        phone: request.phone
                    };
                    break;
                case 'facility':
                    endpoint = '/facility/register';
                    payload = {
                        facilityName: request.facilityName,
                        facilityDescription: request.facilityDescription
                    };
                    break;
                case 'hospital':
                    endpoint = '/hospital/register';
                    payload = {
                        hospitalName: request.hospitalName,
                        hospitalType: request.hospitalType,
                        hospitalYearOfEstablishment: request.hospitalYearOfEstablishment,
                        hospitalNumOfUsersServed: request.hospitalNumOfUsersServed,
                        hospitalContact: request.hospitalContact,
                        hospitalAddress: request.hospitalAddress,
                        countryName: request.countryName,
                        stateName: request.stateName,
                        facilities: request.facilities
                    };
                    break;
            }

            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const result = await response.text();
                this.showToast(`${type.charAt(0).toUpperCase() + type.slice(1)} approved successfully: ${result}`, 'success');

                // Remove from pending requests
                this.removePendingRequest(requestId, type);
                this.refreshCurrentSection();
                this.updateOverviewStats();
            } else {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
            }
        } catch (error) {
            console.error(`Error approving ${type}:`, error);
            this.showToast(`Failed to approve ${type}. Error: ${error.message}`, 'error');
        } finally {
            this.showLoading(false);
        }
    }

    async rejectRequest(requestId, type) {
        // In a real application, you might want to send a rejection notification
        // or store the rejection reason. For now, we'll just remove it locally
        if (confirm(`Are you sure you want to reject this ${type} request?`)) {
            this.showToast(`${type.charAt(0).toUpperCase() + type.slice(1)} request rejected`, 'warning');

            // Remove from pending requests
            this.removePendingRequest(requestId, type);
            this.refreshCurrentSection();
            this.updateOverviewStats();

            // In a real implementation, you might want to call an API endpoint
            // await fetch(`/api/admin/${type}-requests/${requestId}/reject`, { method: 'POST' });
        }
    }

    removePendingRequest(requestId, type) {
        const key = type + 's';
        this.pendingRequests[key] = this.pendingRequests[key].filter(
            req => req.id.toString() !== requestId.toString()
        );
    }

    refreshCurrentSection() {
        switch(this.currentSection) {
            case 'doctor-register':
                this.renderDoctorRequests(this.pendingRequests.doctors);
                break;
            case 'facility-register':
                this.renderFacilityRequests(this.pendingRequests.facilities);
                break;
            case 'hospital-register':
                this.renderHospitalRequests(this.pendingRequests.hospitals);
                break;
        }
    }

    async handleCountrySubmission(e) {
        e.preventDefault();

        const formData = new FormData(e.target);
        const countryData = {
            countryName: formData.get('countryName').trim()
        };

        if (!countryData.countryName) {
            this.showToast('Please enter a country name', 'error');
            return;
        }

        this.showLoading(true);

        try {
            const response = await fetch('/country/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(countryData)
            });

            if (response.ok) {
                const result = await response.text();
                this.showToast(`Country registered successfully: ${result}`, 'success');
                e.target.reset();
                // Reset the countries loaded flag so it refreshes with new country
                this.countriesLoaded = false;
            } else {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
            }
        } catch (error) {
            console.error('Error registering country:', error);
            this.showToast(`Failed to register country. Error: ${error.message}`, 'error');
        } finally {
            this.showLoading(false);
        }
    }

    async handleStateSubmission(e) {
        e.preventDefault();

        const formData = new FormData(e.target);
        const stateData = {
            stateName: formData.get('stateName').trim(),
            countryName: formData.get('countryName')
        };

        if (!stateData.stateName || !stateData.countryName) {
            this.showToast('Please fill in all fields', 'error');
            return;
        }

        this.showLoading(true);

        try {
            const response = await fetch('/state/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(stateData)
            });

            if (response.ok) {
                const result = await response.text();
                this.showToast(`State registered successfully: ${result}`, 'success');
                e.target.reset();
            } else {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
            }
        } catch (error) {
            console.error('Error registering state:', error);
            this.showToast(`Failed to register state. Error: ${error.message}`, 'error');
        } finally {
            this.showLoading(false);
        }
    }

    updateOverviewStats() {
        document.getElementById('pending-doctors').textContent = this.pendingRequests.doctors.length;
        document.getElementById('pending-facilities').textContent = this.pendingRequests.facilities.length;
        document.getElementById('pending-hospitals').textContent = this.pendingRequests.hospitals.length;

        // You might want to fetch actual count for countries from API
        document.getElementById('total-countries').textContent = '8'; // Based on mock data
    }

    showLoading(show) {
        const spinner = document.getElementById('loading-spinner');
        if (show) {
            spinner.classList.add('show');
        } else {
            spinner.classList.remove('show');
        }
    }

    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;

        const icon = type === 'success' ? 'check-circle' :
                     type === 'error' ? 'exclamation-circle' :
                     'exclamation-triangle';

        toast.innerHTML = `
            <i class="fas fa-${icon}"></i>
            <span>${message}</span>
        `;

        container.appendChild(toast);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 5000);

        // Add click to close functionality
        toast.addEventListener('click', () => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        });
    }

    // Additional utility methods
    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }

    validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    validatePhone(phone) {
        const phoneRegex = /^[+]?[\d\s-()]+$/;
        return phoneRegex.test(phone);
    }

    // Method to manually refresh all data
    async refreshAllData() {
        this.showLoading(true);
        try {
            await this.loadInitialData();
            this.showToast('Dashboard data refreshed successfully', 'success');
        } catch (error) {
            this.showToast('Failed to refresh dashboard data', 'error');
        } finally {
            this.showLoading(false);
        }
    }
}

// Global functions for onclick handlers (needed for HTML onclick attributes)
window.loadDoctorRequests = function() {
    if (window.adminDashboard) {
        window.adminDashboard.loadDoctorRequests();
    }
};

window.loadFacilityRequests = function() {
    if (window.adminDashboard) {
        window.adminDashboard.loadFacilityRequests();
    }
};

window.loadHospitalRequests = function() {
    if (window.adminDashboard) {
        window.adminDashboard.loadHospitalRequests();
    }
};

window.closeModal = function() {
    if (window.adminDashboard) {
        window.adminDashboard.closeModal();
    }
};

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    window.adminDashboard = new AdminDashboard();

    // Add keyboard shortcuts
    document.addEventListener('keydown', function(e) {
        // Ctrl/Cmd + R to refresh current section
        if ((e.ctrlKey || e.metaKey) && e.key === 'r') {
            e.preventDefault();
            if (window.adminDashboard) {
                window.adminDashboard.refreshAllData();
            }
        }

        // Ctrl/Cmd + 1-6 for quick navigation
        if ((e.ctrlKey || e.metaKey) && e.key >= '1' && e.key <= '6') {
            e.preventDefault();
            const sections = ['overview', 'doctor-register', 'facility-register', 'hospital-register', 'country-register', 'state-register'];
            const sectionIndex = parseInt(e.key) - 1;
            if (sections[sectionIndex] && window.adminDashboard) {
                window.adminDashboard.showSection(sections[sectionIndex]);
            }
        }
    });

    // Handle browser back/forward buttons
    window.addEventListener('popstate', function(e) {
        if (e.state && e.state.section && window.adminDashboard) {
            window.adminDashboard.showSection(e.state.section);
        }
    });

    // Optional: Add visibility change handler to refresh data when tab becomes visible
    document.addEventListener('visibilitychange', function() {
        if (!document.hidden && window.adminDashboard) {
            // Optionally refresh data when user returns to tab
            // window.adminDashboard.refreshAllData();
        }
    });
});

// Add some additional utility functions that might be useful
window.AdminDashboardUtils = {
    // Function to export pending requests as JSON (for debugging/backup)
    exportPendingRequests: function() {
        if (window.adminDashboard) {
            const data = window.adminDashboard.pendingRequests;
            const jsonString = JSON.stringify(data, null, 2);
            const blob = new Blob([jsonString], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'pending-requests-' + new Date().toISOString().split('T')[0] + '.json';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }
    },

    // Function to clear all pending requests (for testing)
    clearAllPendingRequests: function() {
        if (window.adminDashboard && confirm('Are you sure you want to clear all pending requests? This action cannot be undone.')) {
            window.adminDashboard.pendingRequests = {
                doctors: [],
                facilities: [],
                hospitals: []
            };
            window.adminDashboard.refreshCurrentSection();
            window.adminDashboard.updateOverviewStats();
            window.adminDashboard.showToast('All pending requests cleared', 'warning');
        }
    },

    // Function to simulate adding new requests (for testing)
    addMockRequests: function() {
        if (window.adminDashboard) {
            const mockDoctor = {
                id: Date.now(),
                doctorName: 'Dr. Test User',
                specialization: 'General Medicine',
                experience: '5 years',
                qualification: 'MBBS',
                email: 'test@example.com',
                phone: '+1234567890',
                requestDate: new Date().toISOString().split('T')[0]
            };

            const mockFacility = {
                id: Date.now() + 1,
                facilityName: 'Test Facility',
                facilityDescription: 'Test facility for demonstration purposes',
                requestDate: new Date().toISOString().split('T')[0]
            };

            const mockHospital = {
                id: Date.now() + 2,
                hospitalName: 'Test Hospital',
                hospitalType: 'Private',
                hospitalYearOfEstablishment: 2020,
                hospitalNumOfUsersServed: 1000,
                hospitalContact: '+1234567890',
                hospitalAddress: 'Test Address',
                countryName: 'Test Country',
                stateName: 'Test State',
                facilities: ['Emergency Care', 'General Ward'],
                requestDate: new Date().toISOString().split('T')[0]
            };

            window.adminDashboard.pendingRequests.doctors.push(mockDoctor);
            window.adminDashboard.pendingRequests.facilities.push(mockFacility);
            window.adminDashboard.pendingRequests.hospitals.push(mockHospital);

            window.adminDashboard.refreshCurrentSection();
            window.adminDashboard.updateOverviewStats();
            window.adminDashboard.showToast('Mock requests added successfully', 'success');
        }
    }
};