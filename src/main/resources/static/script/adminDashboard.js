// Admin Dashboard JavaScript
class AdminDashboard {
    constructor() {
        this.baseURL = '';
        this.pendingDoctorApplications = [];
        this.pendingHospitalApplications = [];
        this.countryList = [];
        this.adminName = 'Admin'; // Default fallback
        this.init();
    }

    init() {
        this.setupEventListeners();
        this.loadInitialData();
    }

    setupEventListeners() {
        // Sidebar navigation
        document.querySelectorAll('.menu-item').forEach(item => {
            item.addEventListener('click', (e) => {
                this.switchSection(e.currentTarget.dataset.section);
            });
        });

        // Form submissions
        document.getElementById('facility-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.submitFacilityForm();
        });

        document.getElementById('country-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.submitCountryForm();
        });

        document.getElementById('state-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.submitStateForm();
        });

        // Country dropdown change
        document.getElementById('stateCountry').addEventListener('focus', () => {
            this.loadCountries();
        });
    }

    switchSection(section) {
        // Update active menu item
        document.querySelectorAll('.menu-item').forEach(item => {
            item.classList.remove('active');
        });
        document.querySelector(`[data-section="${section}"]`).classList.add('active');

        // Show corresponding section
        document.querySelectorAll('.content-section').forEach(section => {
            section.classList.remove('active');
        });
        document.getElementById(section).classList.add('active');

        // Load section-specific data
        if (section === 'doctor-registration') {
            this.renderDoctorCards('doctor-list');
        } else if (section === 'hospital-registration') {
            this.renderHospitalCards('hospital-list');
        }
    }

    async loadInitialData() {
        this.showLoading(true);
        try {
            const response = await fetch('/adminHome');
            if (response.ok) {
                const data = await response.json();
                this.pendingDoctorApplications = data.pendingDoctorApplications || [];
                this.pendingHospitalApplications = data.pendingHospitalApplications || [];

                // Update admin name dynamically
                if (data.adminName) {
                    this.adminName = data.adminName;
                    this.updateWelcomeText();
                }

                this.renderDoctorCards('doctor-applications');
                this.renderHospitalCards('hospital-applications');
            } else {
                throw new Error('Failed to load dashboard data');
            }
        } catch (error) {
            this.showToast('Failed to load dashboard data', 'error');
            console.error('Error loading dashboard data:', error);
        } finally {
            this.showLoading(false);
        }
    }

    updateWelcomeText() {
        const welcomeElement = document.getElementById('welcome-text');
        if (welcomeElement) {
            welcomeElement.textContent = `Welcome, ${this.adminName}`;
        }
    }

    renderDoctorCards(containerId) {
        const container = document.getElementById(containerId);
        container.innerHTML = '';

        if (this.pendingDoctorApplications.length === 0) {
            container.innerHTML = '<div style="color: white; text-align: center; padding: 2rem;">No pending doctor applications</div>';
            return;
        }

        this.pendingDoctorApplications.forEach(doctor => {
            const card = this.createDoctorCard(doctor);
            container.appendChild(card);
        });
    }

    createDoctorCard(doctor) {
        const card = document.createElement('div');
        card.className = 'doctor-card';

        const facilitiesHtml = doctor.medicalFacilities
            ? doctor.medicalFacilities.map(facility =>
                `<span class="facility-tag">${facility}</span>`
              ).join('')
            : '';

        card.innerHTML = `
            <div class="card-header">
                <i class="fas fa-user-md"></i>
                <div class="card-title">Dr. ${doctor.tmpDoctorName}</div>
            </div>
            <div class="card-content">
                <div class="card-field">
                    <span class="field-label">Specialization:</span>
                    <span class="field-value">${doctor.tmpDoctorFieldOfExpertise}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Experience:</span>
                    <span class="field-value">${doctor.tmpDoctorYearsOfExperience} years</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Age:</span>
                    <span class="field-value">${doctor.tmpDoctorAge}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Gender:</span>
                    <span class="field-value">${doctor.tmpDoctorGender}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">College:</span>
                    <span class="field-value">${doctor.tmpDoctorGraduateCollege}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Email:</span>
                    <span class="field-value">${doctor.tmpDoctorEmail}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Mobile:</span>
                    <span class="field-value">${doctor.tmpDoctorMobile}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Type:</span>
                    <span class="field-value">${doctor.tmpDoctorType}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Hospital Applied:</span>
                    <span class="field-value">${doctor.hospitalAppliedFor}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Location:</span>
                    <span class="field-value">${doctor.tmpDoctorDetailAddress}, ${doctor.tmpDoctorStateName}, ${doctor.tmpDoctorCountryName}</span>
                </div>
                ${facilitiesHtml ? `<div class="card-field">
                    <span class="field-label">Medical Facilities:</span>
                    <div class="facilities-list">${facilitiesHtml}</div>
                </div>` : ''}
            </div>
            <div class="card-actions">
                <button class="approve-btn" onclick="adminDashboard.approveDoctor('${doctor.tmpDoctorEmail}')">
                    <i class="fas fa-check"></i> Approve
                </button>
                <button class="remove-btn" onclick="adminDashboard.removeDoctor('${doctor.tmpDoctorEmail}')">
                    <i class="fas fa-times"></i> Reject
                </button>
            </div>
        `;

        return card;
    }

    renderHospitalCards(containerId) {
        const container = document.getElementById(containerId);
        container.innerHTML = '';

        if (this.pendingHospitalApplications.length === 0) {
            container.innerHTML = '<div style="color: white; text-align: center; padding: 2rem;">No pending hospital applications</div>';
            return;
        }

        this.pendingHospitalApplications.forEach(hospital => {
            const card = this.createHospitalCard(hospital);
            container.appendChild(card);
        });
    }

    createHospitalCard(hospital) {
        const card = document.createElement('div');
        card.className = 'hospital-card';

        const facilitiesHtml = hospital.medicalFacilities
            ? hospital.medicalFacilities.map(facility =>
                `<span class="facility-tag">${facility}</span>`
              ).join('')
            : '';

        card.innerHTML = `
            <div class="card-header">
                <i class="fas fa-hospital"></i>
                <div class="card-title">${hospital.tempHospitalName}</div>
            </div>
            <div class="card-content">
                <div class="card-field">
                    <span class="field-label">Type:</span>
                    <span class="field-value">${hospital.tempHospitalType}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Established:</span>
                    <span class="field-value">${hospital.tempHospitalYearOfEstablishment}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Users Served:</span>
                    <span class="field-value">${hospital.tempHospitalNumOfUsersServed.toLocaleString()}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Rating:</span>
                    <span class="field-value">${hospital.tempHospitalRating}/5.0</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Contact:</span>
                    <span class="field-value">${hospital.tempHospitalContact}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Address:</span>
                    <span class="field-value">${hospital.tempHospitalAddress}</span>
                </div>
                <div class="card-field">
                    <span class="field-label">Location:</span>
                    <span class="field-value">${hospital.tempStateName}, ${hospital.tempCountryName}</span>
                </div>
                ${facilitiesHtml ? `<div class="card-field">
                    <span class="field-label">Medical Facilities:</span>
                    <div class="facilities-list">${facilitiesHtml}</div>
                </div>` : ''}
            </div>
            <div class="card-actions">
                <button class="approve-btn" onclick="adminDashboard.approveHospital('${hospital.tempHospitalName}')">
                    <i class="fas fa-check"></i> Approve
                </button>
                <button class="remove-btn" onclick="adminDashboard.removeHospital('${hospital.tempHospitalName}')">
                    <i class="fas fa-times"></i> Reject
                </button>
            </div>
        `;

        return card;
    }

    async approveDoctor(email) {
        this.showLoading(true);
        try {
            const response = await fetch(`/doctor/register?email=${encodeURIComponent(email)}`, {
                method: 'POST'
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                this.refreshData();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to approve doctor', 'error');
            console.error('Error approving doctor:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async removeDoctor(email) {
        this.showLoading(true);
        try {
            const response = await fetch('/contactUs/doctorRemovalRequest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(email)
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                this.refreshData();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to remove doctor application', 'error');
            console.error('Error removing doctor:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async approveHospital(hospitalName) {
        this.showLoading(true);
        try {
            const response = await fetch(`/hospital/register?hospitalName=${encodeURIComponent(hospitalName)}`, {
                method: 'POST'
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                this.refreshData();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to approve hospital', 'error');
            console.error('Error approving hospital:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async removeHospital(hospitalName) {
        this.showLoading(true);
        try {
            const response = await fetch('/contactUs/hospitalRemovalRequest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(hospitalName)
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                this.refreshData();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to remove hospital application', 'error');
            console.error('Error removing hospital:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async submitFacilityForm() {
        const formData = new FormData(document.getElementById('facility-form'));
        const facilityData = {
            facilityName: formData.get('facilityName'),
            facilityDescription: formData.get('facilityDescription')
        };

        this.showLoading(true);
        try {
            const response = await fetch('/facility/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(facilityData)
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                document.getElementById('facility-form').reset();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to add facility', 'error');
            console.error('Error adding facility:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async submitCountryForm() {
        const formData = new FormData(document.getElementById('country-form'));
        const countryData = {
            countryName: formData.get('countryName')
        };

        this.showLoading(true);
        try {
            const response = await fetch('/country/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(countryData)
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                document.getElementById('country-form').reset();
                // Clear country list to force reload
                this.countryList = [];
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to add country', 'error');
            console.error('Error adding country:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async loadCountries() {
        if (this.countryList.length > 0) return;

        try {
            const response = await fetch('/country/getCountryList');
            if (response.ok) {
                this.countryList = await response.json();
                this.populateCountryDropdown();
            } else {
                this.showToast('Failed to load countries', 'error');
            }
        } catch (error) {
            this.showToast('Failed to load countries', 'error');
            console.error('Error loading countries:', error);
        }
    }

    populateCountryDropdown() {
        const select = document.getElementById('stateCountry');
        select.innerHTML = '<option value="">Select a country</option>';

        this.countryList.forEach(country => {
            const option = document.createElement('option');
            option.value = country.countryName;
            option.textContent = country.countryName;
            select.appendChild(option);
        });
    }

    async submitStateForm() {
        const formData = new FormData(document.getElementById('state-form'));
        const stateData = {
            stateName: formData.get('stateName'),
            countryName: formData.get('countryName')
        };

        this.showLoading(true);
        try {
            const response = await fetch('/state/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(stateData)
            });

            if (response.ok) {
                const message = await response.text();
                this.showToast(message, 'success');
                document.getElementById('state-form').reset();
            } else {
                const errorMessage = await response.text();
                this.showToast(errorMessage, 'error');
            }
        } catch (error) {
            this.showToast('Failed to add state', 'error');
            console.error('Error adding state:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async logout() {
        this.showLoading(true);
        try {
            const response = await fetch('/logout', {
                method: 'POST'
            });

            if (response.ok) {
                this.showToast('Logout successful', 'success');
                // Redirect to login page or home page after a short delay
                setTimeout(() => {
                    window.location.href = '/'; // Adjust redirect URL as needed
                }, 1500);
            } else {
                this.showToast('Logout failed', 'error');
            }
        } catch (error) {
            this.showToast('Logout failed', 'error');
            console.error('Error during logout:', error);
        } finally {
            this.showLoading(false);
        }
    }

    async refreshData() {
        await this.loadInitialData();
    }

    showLoading(show) {
        const overlay = document.getElementById('loading-overlay');
        if (show) {
            overlay.classList.add('show');
        } else {
            overlay.classList.remove('show');
        }
    }

    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.textContent = message;

        container.appendChild(toast);

        // Auto remove toast after 5 seconds
        setTimeout(() => {
            if (toast.parentNode) {
                toast.remove();
            }
        }, 5000);

        // Add click to dismiss
        toast.addEventListener('click', () => {
            toast.remove();
        });
    }
}

// Global functions for onclick handlers
window.refreshData = function() {
    if (window.adminDashboard) {
        window.adminDashboard.refreshData();
    }
};

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.adminDashboard = new AdminDashboard();
});