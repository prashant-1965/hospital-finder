// Government Hospital Dashboard JavaScript
class GovHospitalDashboard {
    constructor() {
        this.apiEndpoint = '/govHospital';
        this.doctorDetailsEndpoint = '/doctor/getDoctorDetails';
        this.doctorsData = [];
        this.hospitalsData = [];
        this.customData = window.customData || {};
        this.init();
    }

    async init() {
        try {
            this.updateLocationInfo();
            this.setupEventListeners();
            await this.loadDashboardData();
            this.setupScrollAnimations();
        } catch (error) {
            console.error('Failed to initialize dashboard:', error);
            this.showError('Failed to load dashboard. Please refresh the page.');
        }
    }

    updateLocationInfo() {
        // Update current location display
        const locationElement = document.getElementById('currentLocation');
        const specializationElement = document.getElementById('currentSpecialization');

        if (locationElement && this.customData.country && this.customData.state) {
            locationElement.textContent = `${this.customData.country}, ${this.customData.state}`;
        }

        if (specializationElement && this.customData.specialisation) {
            specializationElement.textContent = this.customData.specialisation;
        }
    }

    async loadDashboardData() {
        try {
            // Build query parameters from customData
            const urlParams = new URLSearchParams();
            Object.keys(this.customData).forEach(key => {
                if (this.customData[key]) {
                    urlParams.append(key, this.customData[key]);
                }
            });

            const response = await fetch(`${this.apiEndpoint}?${urlParams.toString()}`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            // Store data for modal access
            this.doctorsData = data.topNGovSpecialistDoctor || [];
            this.hospitalsData = data.topNGovSpecialistHospitals || [];

            // Update location display from API response
            if (data['Current Location: ']) {
                const locationElement = document.getElementById('currentLocation');
                if (locationElement) {
                    locationElement.textContent = data['Current Location: '];
                }
            }

            // Update stats
            this.updateStats();

            // Render doctors list
            this.renderDoctorsList(this.doctorsData);

            // Render hospitals list
            this.renderHospitalsList(this.hospitalsData);

        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Unable to load dashboard data. Please check your connection and try again.');
            this.hideLoadingStates();
        }
    }

    updateStats() {
        // Update doctors count
        const doctorsCountElement = document.getElementById('doctorsCount');
        if (doctorsCountElement) {
            doctorsCountElement.textContent = this.doctorsData.length;
        }

        // Update hospitals count
        const hospitalsCountElement = document.getElementById('hospitalsCount');
        if (hospitalsCountElement) {
            hospitalsCountElement.textContent = this.hospitalsData.length;
        }

        // Calculate and update average rating
        const averageRatingElement = document.getElementById('averageRating');
        if (averageRatingElement) {
            let totalRating = 0;
            let ratingCount = 0;

            // Add doctor ratings
            this.doctorsData.forEach(doctor => {
                if (doctor.doctorRatting) {
                    totalRating += doctor.doctorRatting;
                    ratingCount++;
                }
            });

            // Add hospital ratings
            this.hospitalsData.forEach(hospital => {
                if (hospital.hospitalRating) {
                    totalRating += hospital.hospitalRating;
                    ratingCount++;
                }
            });

            const averageRating = ratingCount > 0 ? (totalRating / ratingCount).toFixed(1) : '0.0';
            averageRatingElement.textContent = averageRating;
        }
    }

    renderDoctorsList(doctors) {
        const loadingElement = document.getElementById('doctorsLoading');
        const listElement = document.getElementById('doctorsList');

        if (loadingElement) loadingElement.style.display = 'none';

        if (!doctors || doctors.length === 0) {
            listElement.innerHTML = this.getEmptyState('No government specialist doctors found', 'fa-user-md');
            return;
        }

        // Create enough duplicates for seamless loop
        const duplicatedDoctors = [];
        for (let i = 0; i < 6; i++) {
            duplicatedDoctors.push(...doctors);
        }

        listElement.innerHTML = duplicatedDoctors.map((doctor, index) => `
            <div class="doctor-card" data-doctor-id="${index % doctors.length}">
                <div class="card-image-section">
                    <div>
                        <i class="fas fa-user-md"></i>
                        <div class="coming-soon">Public</div>
                    </div>
                </div>
                <div class="card-content-section">
                    <div class="card-title">${this.escapeHtml(doctor.doctorName || 'Unknown Doctor')}</div>
                    <div class="card-info">
                        <div class="card-info-item">
                            <i class="fas fa-stethoscope"></i>
                            ${this.escapeHtml(doctor.doctorFieldOfExpertise || 'General Medicine')}
                        </div>
                        <div class="card-info-item">
                            <i class="fas fa-graduation-cap"></i>
                            ${doctor.yearOfExp || doctor.doctorYearsOfExperience || 0} years experience
                        </div>
                        <div class="card-info-item">
                            <i class="fas fa-hand-holding-heart"></i>
                            Government Service
                        </div>
                        <div class="card-rating">
                            ${this.generateStars(doctor.doctorRatting || doctor.doctorRating || 0)}
                            <span class="rating-text">(${doctor.doctorRatting || doctor.doctorRating || 0}/5)</span>
                        </div>
                    </div>
                    <button class="get-details-btn" onclick="handleGetDetails('doctor', ${index % doctors.length})">
                        Get More Details
                    </button>
                </div>
            </div>
        `).join('');
    }

    renderHospitalsList(hospitals) {
        const loadingElement = document.getElementById('hospitalsLoading');
        const listElement = document.getElementById('hospitalsList');

        if (loadingElement) loadingElement.style.display = 'none';

        if (!hospitals || hospitals.length === 0) {
            listElement.innerHTML = this.getEmptyState('No government hospitals found', 'fa-hospital');
            return;
        }

        // Create enough duplicates for seamless loop
        const duplicatedHospitals = [];
        for (let i = 0; i < 6; i++) {
            duplicatedHospitals.push(...hospitals);
        }

        listElement.innerHTML = duplicatedHospitals.map((hospital, index) => `
            <div class="hospital-card" data-hospital-id="${index % hospitals.length}">
                <div class="card-image-section">
                    <div>
                        <i class="fas fa-hospital"></i>
                        <div class="coming-soon">Public</div>
                    </div>
                </div>
                <div class="card-content-section">
                    <div class="card-title">${this.escapeHtml(hospital.hospitalName || 'Unknown Hospital')}</div>
                    <div class="card-info">
                        <div class="card-info-item">
                            <i class="fas fa-building"></i>
                            ${this.escapeHtml(hospital.hospitalType || 'Government Hospital')}
                        </div>
                        <div class="card-info-item">
                            <i class="fas fa-map-marker-alt"></i>
                            ${this.escapeHtml(hospital.hospitalLocation || hospital.location || 'Location not specified')}
                        </div>
                        <div class="card-info-item">
                            <i class="fas fa-hand-holding-heart"></i>
                            Free Public Healthcare
                        </div>
                        <div class="card-rating">
                            ${this.generateStars(hospital.hospitalRating || 0)}
                            <span class="rating-text">(${hospital.hospitalRating || 0}/5)</span>
                        </div>
                    </div>
                    <button class="get-details-btn" onclick="handleGetDetails('hospital', ${index % hospitals.length})">
                        Get More Details
                    </button>
                </div>
            </div>
        `).join('');
    }

    setupScrollAnimations() {
        const scrollingLists = [
            document.getElementById('doctorsList'),
            document.getElementById('hospitalsList')
        ];

        scrollingLists.forEach(list => {
            if (list) {
                list.addEventListener('mouseenter', () => {
                    list.style.animationPlayState = 'paused';
                });

                list.addEventListener('mouseleave', () => {
                    list.style.animationPlayState = 'running';
                });

                const cards = list.querySelectorAll('.doctor-card, .hospital-card');
                cards.forEach(card => {
                    card.addEventListener('mouseenter', () => {
                        list.style.animationPlayState = 'paused';
                    });

                    card.addEventListener('mouseleave', () => {
                        list.style.animationPlayState = 'running';
                    });
                });
            }
        });
    }

    async loadDoctorDetails(doctorName) {
        try {
            const response = await fetch(`${this.doctorDetailsEndpoint}?doctorName=${encodeURIComponent(doctorName)}`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const doctorDetails = await response.json();
            this.renderDoctorDetailsModal(doctorDetails);
        } catch (error) {
            console.error('Error loading doctor details:', error);
            this.showDoctorDetailsError('Unable to load doctor details. Please try again.');
        }
    }

    renderDoctorDetailsModal(doctorDetails) {
        const modalContent = document.getElementById('doctorDetailsContent');

        modalContent.innerHTML = `
            <div class="doctor-details-header">
                <div class="doctor-avatar">
                    <i class="fas fa-user-md"></i>
                </div>
                <div class="doctor-name">${this.escapeHtml(doctorDetails.doctorName || 'Unknown Doctor')}</div>
                <div class="doctor-specialty">${this.escapeHtml(doctorDetails.doctorFieldOfExpertise || 'General Medicine')}</div>
                <div style="background: linear-gradient(135deg, #2E8B57, #228B22); color: white; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.9rem; margin-top: 1rem; display: inline-block;">
                    <i class="fas fa-hand-holding-heart"></i> Government Service
                </div>
            </div>

            <div class="doctor-details-grid">
                <div class="detail-card">
                    <div class="detail-label">Age</div>
                    <div class="detail-value">
                        <i class="fas fa-birthday-cake"></i>
                        <span class="age-badge">${doctorDetails.doctorAge || 'N/A'} years</span>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Gender</div>
                    <div class="detail-value">
                        <i class="fas ${doctorDetails.doctorGender?.toLowerCase() === 'female' ? 'fa-venus' : 'fa-mars'}"></i>
                        <span class="gender-badge">${this.escapeHtml(doctorDetails.doctorGender || 'Not specified')}</span>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Experience</div>
                    <div class="detail-value">
                        <i class="fas fa-graduation-cap"></i>
                        <span class="experience-badge">${doctorDetails.doctorYearsOfExperience || 0} years</span>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Rating</div>
                    <div class="detail-value">
                        <i class="fas fa-star"></i>
                        <div class="rating-display">
                            ${this.generateStars(doctorDetails.doctorRating || 0)}
                            <span class="rating-text">(${doctorDetails.doctorRating || 0}/5)</span>
                        </div>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Education</div>
                    <div class="detail-value">
                        <i class="fas fa-university"></i>
                        ${this.escapeHtml(doctorDetails.doctorGraduateCollege || 'Not specified')}
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Doctor Type</div>
                    <div class="detail-value">
                        <i class="fas fa-stethoscope"></i>
                        ${this.escapeHtml(doctorDetails.doctorType || 'Government Doctor')}
                    </div>
                </div>
            </div>
        `;
    }

    showDoctorDetailsError(message) {
        const modalContent = document.getElementById('doctorDetailsContent');
        modalContent.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-exclamation-triangle"></i>
                <p>${message}</p>
            </div>
        `;
    }

    renderHospitalDetailsModal(hospital) {
        const modalContent = document.getElementById('hospitalDetailsContent');

        modalContent.innerHTML = `
            <div class="doctor-details-header">
                <div class="doctor-avatar">
                    <i class="fas fa-hospital"></i>
                </div>
                <div class="doctor-name">${this.escapeHtml(hospital.hospitalName || 'Unknown Hospital')}</div>
                <div class="doctor-specialty">${this.escapeHtml(hospital.hospitalType || 'Government Hospital')}</div>
                <div style="background: linear-gradient(135deg, #2E8B57, #228B22); color: white; padding: 0.5rem 1rem; border-radius: 20px; font-size: 0.9rem; margin-top: 1rem; display: inline-block;">
                    <i class="fas fa-hand-holding-heart"></i> Public Healthcare
                </div>
            </div>

            <div class="doctor-details-grid">
                <div class="detail-card">
                    <div class="detail-label">Hospital Type</div>
                    <div class="detail-value">
                        <i class="fas fa-building"></i>
                        <span class="age-badge">${this.escapeHtml(hospital.hospitalType || 'Government')}</span>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Location</div>
                    <div class="detail-value">
                        <i class="fas fa-map-marker-alt"></i>
                        ${this.escapeHtml(hospital.hospitalLocation || hospital.location || 'Not specified')}
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Rating</div>
                    <div class="detail-value">
                        <i class="fas fa-star"></i>
                        <div class="rating-display">
                            ${this.generateStars(hospital.hospitalRating || 0)}
                            <span class="rating-text">(${hospital.hospitalRating || 0}/5)</span>
                        </div>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Specialization</div>
                    <div class="detail-value">
                        <i class="fas fa-stethoscope"></i>
                        ${this.escapeHtml(hospital.specialization || this.customData.specialisation || 'Multi-specialty')}
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Healthcare Access</div>
                    <div class="detail-value">
                        <i class="fas fa-hand-holding-heart"></i>
                        Free Public Healthcare
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-label">Status</div>
                    <div class="detail-value">
                        <i class="fas fa-check-circle"></i>
                        <span class="experience-badge">Active</span>
                    </div>
                </div>
            </div>
        `;
    }

    setupEventListeners() {
        // Customize button
        const customizeBtn = document.getElementById('customizeBtn');
        if (customizeBtn) {
            customizeBtn.addEventListener('click', () => {
                window.location.href = '/dashboard/patient';
            });
        }

        // Refresh buttons
        const refreshDoctorsBtn = document.getElementById('refreshDoctors');
        if (refreshDoctorsBtn) {
            refreshDoctorsBtn.addEventListener('click', () => {
                this.refreshDashboard();
            });
        }

        const refreshHospitalsBtn = document.getElementById('refreshHospitals');
        if (refreshHospitalsBtn) {
            refreshHospitalsBtn.addEventListener('click', () => {
                this.refreshDashboard();
            });
        }

        // Modal close handlers
        const doctorModal = document.getElementById('doctorDetailsModal');
        const closeDoctorBtn = document.querySelector('.close-doctor-details');
        const hospitalModal = document.getElementById('hospitalDetailsModal');
        const closeHospitalBtn = document.querySelector('.close-hospital-details');
        const errorModal = document.getElementById('errorModal');
        const closeErrorBtn = document.getElementById('closeErrorModal');

        if (closeDoctorBtn) {
            closeDoctorBtn.onclick = () => {
                if (doctorModal) doctorModal.style.display = 'none';
            };
        }

        if (closeHospitalBtn) {
            closeHospitalBtn.onclick = () => {
                if (hospitalModal) hospitalModal.style.display = 'none';
            };
        }

        if (closeErrorBtn) {
            closeErrorBtn.onclick = () => {
                if (errorModal) errorModal.style.display = 'none';
            };
        }

        // Close modals when clicking outside
        window.onclick = (event) => {
            if (event.target === doctorModal) {
                doctorModal.style.display = 'none';
            }
            if (event.target === hospitalModal) {
                hospitalModal.style.display = 'none';
            }
            if (event.target === errorModal) {
                errorModal.style.display = 'none';
            }
        };

        // Auto-refresh every 10 minutes
        setInterval(() => {
            this.refreshDashboard();
        }, 600000);
    }

    async refreshDashboard() {
        try {
            await this.loadDashboardData();
            console.log('Government hospital dashboard refreshed successfully');
        } catch (error) {
            console.error('Failed to refresh dashboard:', error);
        }
    }

    generateStars(rating) {
        const fullStars = Math.floor(rating);
        const hasHalfStar = rating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        let starsHtml = '<div class="stars">';

        for (let i = 0; i < fullStars; i++) {
            starsHtml += '<i class="fas fa-star star"></i>';
        }

        if (hasHalfStar) {
            starsHtml += '<i class="fas fa-star-half-alt star"></i>';
        }

        for (let i = 0; i < emptyStars; i++) {
            starsHtml += '<i class="far fa-star star empty"></i>';
        }

        starsHtml += '</div>';
        return starsHtml;
    }

    getEmptyState(message, iconClass) {
        return `
            <div class="empty-state">
                <i class="fas ${iconClass}"></i>
                <p>${message}</p>
            </div>
        `;
    }

    hideLoadingStates() {
        const loadingElements = [
            'doctorsLoading',
            'hospitalsLoading'
        ];

        loadingElements.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.style.display = 'none';
        });
    }

    showError(message) {
        const modal = document.getElementById('errorModal');
        const errorMessage = document.getElementById('errorMessage');

        if (modal && errorMessage) {
            errorMessage.textContent = message;
            modal.style.display = 'block';
        }
    }

    showDoctorDetailsModal(doctorName) {
        const modal = document.getElementById('doctorDetailsModal');
        const content = document.getElementById('doctorDetailsContent');

        if (modal && content) {
            content.innerHTML = `
                <div class="loading">
                    <i class="fas fa-spinner fa-spin"></i> Loading doctor details...
                </div>
            `;

            modal.style.display = 'block';
            this.loadDoctorDetails(doctorName);
        }
    }

    showHospitalDetailsModal(hospitalIndex) {
        const modal = document.getElementById('hospitalDetailsModal');
        const content = document.getElementById('hospitalDetailsContent');

        if (modal && content && this.hospitalsData[hospitalIndex]) {
            modal.style.display = 'block';
            this.renderHospitalDetailsModal(this.hospitalsData[hospitalIndex]);
        }
    }

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Global reference to dashboard instance
let govDashboardInstance = null;

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    govDashboardInstance = new GovHospitalDashboard();
});

// Global function to handle "Get More Details" button clicks
window.handleGetDetails = function(type, index) {
    console.log(`Get details clicked for ${type} at index ${index}`);

    switch(type) {
        case 'doctor':
            if (govDashboardInstance && govDashboardInstance.doctorsData[index]) {
                const doctor = govDashboardInstance.doctorsData[index];
                govDashboardInstance.showDoctorDetailsModal(doctor.doctorName);
            } else {
                alert('Doctor information not available');
            }
            break;
        case 'hospital':
            if (govDashboardInstance && govDashboardInstance.hospitalsData[index]) {
                govDashboardInstance.showHospitalDetailsModal(index);
            } else {
                alert('Hospital information not available');
            }
            break;
        default:
            console.log('Unknown detail type');
    }
};

// Handle page visibility changes
document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
        setTimeout(() => {
            if (govDashboardInstance) {
                govDashboardInstance.refreshDashboard();
            }
        }, 1000);
    }
});