// Patient Dashboard JavaScript with Customization Modal
class PatientDashboard {
    constructor() {
        this.apiEndpoint = '/dashboard/patient';
        this.doctorDetailsEndpoint = '/doctor/getDoctorDetails';
        this.customApiEndpoint = '/myCustom';
        this.doctorsData = []; // Store doctors data for modal access
        this.hospitalsData = []; // Store hospitals data for modal access
        this.init();
    }

    async init() {
        try {
            await this.loadDashboardData();
            this.setupEventListeners();
            this.setupScrollAnimations();
            await this.loadInitialCustomizationData();
        } catch (error) {
            console.error('Failed to initialize dashboard:', error);
            this.showError('Failed to load dashboard. Please refresh the page.');
        }
    }

    // Updated method in PatientDashboard class
    async loadDashboardData() {
        try {
            const response = await fetch(this.apiEndpoint);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            // Update welcome user name (NEW)
            this.updateWelcomeUser(data.currentUser);

            // Update location details
            this.updateLocationDetails(data.locationDetails);

            // Store data for modal access
            this.doctorsData = data.top5Doctors || [];
            this.hospitalsData = data.top5Hospitals || [];

            // Render doctors list
            this.renderDoctorsList(data.top5Doctors);

            // Render hospitals list
            this.renderHospitalsList(data.top5Hospitals);

            // Render reviews
            this.renderReviewsList(data.top10RattingComment);

            // Setup scroll animations after rendering
            this.setupScrollAnimations();

        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Unable to load dashboard data. Please check your connection and try again.');
            this.hideLoadingStates();
        }
    }

    // NEW method to update welcome user
    updateWelcomeUser(currentUser) {
        const welcomeUserElement = document.querySelector('.welcome-user');
        if (welcomeUserElement && currentUser) {
            welcomeUserElement.textContent = `Welcome ${currentUser}`;
        }
    }

    async loadInitialCustomizationData() {
        try {
            await this.loadCountries();
            await this.loadHospitalTypes();
            await this.loadFacilities();
        } catch (error) {
            console.error('Error loading customization data:', error);
        }
    }

    async loadCountries() {
        try {
            const response = await fetch(`${this.customApiEndpoint}/getCountryList`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const countries = await response.json();

            const countrySelect = document.getElementById('countrySelect');
            countrySelect.innerHTML = '<option value="">Choose a country...</option>';

            countries.forEach(country => {
                const option = document.createElement('option');
                option.value = country.countryName;
                option.textContent = country.countryName;
                countrySelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading countries:', error);
        }
    }

    async loadStates(countryName) {
        try {
            // Show loading state for state dropdown
            const stateSelect = document.getElementById('stateSelect');
            stateSelect.innerHTML = '<option value="">Loading states...</option>';
            stateSelect.disabled = true;

            // Correct URL structure to match your controller endpoint
            const response = await fetch(`${this.customApiEndpoint}/getStateList/${encodeURIComponent(countryName)}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
                // Remove the body parameter - GET requests shouldn't have a body
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const states = await response.json();

            // Clear loading state and populate states
            stateSelect.innerHTML = '<option value="">Choose a state...</option>';

            // Check if states array is empty
            if (!states || states.length === 0) {
                stateSelect.innerHTML = '<option value="">No states found</option>';
                stateSelect.disabled = true;
                return;
            }

            // Populate states dropdown
            states.forEach(state => {
                const option = document.createElement('option');
                // Adjust property name based on your StateListProjection structure
                option.value = state.stateName || state.name || state; // Handle different possible property names
                option.textContent = state.stateName || state.name || state;
                stateSelect.appendChild(option);
            });

            // Enable the dropdown
            stateSelect.disabled = false;

        } catch (error) {
            console.error('Error loading states:', error);

            // Show error state
            const stateSelect = document.getElementById('stateSelect');
            stateSelect.innerHTML = '<option value="">Failed to load states</option>';
            stateSelect.disabled = true;

            this.showError('Failed to load states. Please try again.');
        }
    }

    async loadHospitalTypes() {
        try {
            const response = await fetch(`${this.customApiEndpoint}/hospitalType`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const types = await response.json();
            const hospitalTypeSelect = document.getElementById('hospitalTypeSelect');
            hospitalTypeSelect.innerHTML = '<option value="">Choose hospital type...</option>';

            types.forEach(type => {
                const option = document.createElement('option');
                option.value = type;
                option.textContent = type;
                hospitalTypeSelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading hospital types:', error);
        }
    }

    async loadFacilities() {
        try {
            const response = await fetch(`${this.customApiEndpoint}/allFacilities`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const facilities = await response.json();

            const facilitySelect = document.getElementById('facilitySelect');
            facilitySelect.innerHTML = '<option value="">Choose a facility...</option>';

            facilities.forEach(facility => {
                const option = document.createElement('option');
                option.value = facility.facilityName;
                option.textContent = `${facility.facilityName} - ${facility.facilityDescription}`;
                facilitySelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error loading facilities:', error);
        }
    }

    updateLocationDetails(locationDetails) {
        const locationElement = document.getElementById('locationDetails');
        if (locationElement && locationDetails) {
            locationElement.textContent = locationDetails;
        }
    }

    renderDoctorsList(doctors) {
        const loadingElement = document.getElementById('doctorsLoading');
        const listElement = document.getElementById('doctorsList');

        if (loadingElement) loadingElement.style.display = 'none';

        if (!doctors || doctors.length === 0) {
            listElement.innerHTML = this.getEmptyState('No doctors found', 'fa-user-md');
            return;
        }

        // Create enough duplicates for seamless loop (at least 6 copies for smooth animation)
        const duplicatedDoctors = [];
        for (let i = 0; i < 6; i++) {
            duplicatedDoctors.push(...doctors);
        }

        listElement.innerHTML = duplicatedDoctors.map((doctor, index) => `
            <div class="doctor-card" data-doctor-id="${index % doctors.length}">
                <div class="card-image-section">
                    <div>
                        <i class="fas fa-user-md"></i>
                        <div class="coming-soon">Coming Soon</div>
                    </div>
                </div>
                <div class="card-content-section">
                    <div class="card-title">${this.escapeHtml(doctor.doctorName || 'Unknown Doctor')}</div>
                    <div class="card-info">
                        <div class="card-info-item">
                            <i class="fas fa-graduation-cap"></i>
                            ${doctor.yearOfExp || 0} years experience
                        </div>
                        <div class="card-rating">
                            ${this.generateStars(doctor.doctorRatting || 0)}
                            <span class="rating-text">(${doctor.doctorRatting || 0}/5)</span>
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
            listElement.innerHTML = this.getEmptyState('No hospitals found', 'fa-hospital');
            return;
        }

        // Create enough duplicates for seamless loop (at least 6 copies for smooth animation)
        const duplicatedHospitals = [];
        for (let i = 0; i < 6; i++) {
            duplicatedHospitals.push(...hospitals);
        }

        listElement.innerHTML = duplicatedHospitals.map((hospital, index) => `
            <div class="hospital-card" data-hospital-id="${index % hospitals.length}">
                <div class="card-image-section">
                    <div>
                        <i class="fas fa-hospital"></i>
                        <div class="coming-soon">Coming Soon</div>
                    </div>
                </div>
                <div class="card-content-section">
                    <div class="card-title">${this.escapeHtml(hospital.hospitalName || 'Unknown Hospital')}</div>
                    <div class="card-info">
                        <div class="card-info-item">
                            <i class="fas fa-building"></i>
                            ${this.escapeHtml(hospital.hospitalType || 'General Hospital')}
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

    renderReviewsList(reviews) {
        const loadingElement = document.getElementById('reviewsLoading');
        const listElement = document.getElementById('reviewsList');

        if (loadingElement) loadingElement.style.display = 'none';

        if (!reviews || reviews.length === 0) {
            listElement.innerHTML = this.getEmptyState('No recent reviews found', 'fa-star');
            return;
        }

        // Create enough duplicates for seamless loop (at least 6 copies for smooth animation)
        const duplicatedReviews = [];
        for (let i = 0; i < 6; i++) {
            duplicatedReviews.push(...reviews);
        }

        listElement.innerHTML = duplicatedReviews.map((review, index) => `
            <div class="review-card" data-review-id="${index % reviews.length}">
                <div class="card-image-section">
                    <div>
                        <i class="fas fa-star"></i>
                        <div class="coming-soon">Coming Soon</div>
                    </div>
                </div>
                <div class="card-content-section">
                    <div class="card-title">${this.escapeHtml(review.userName || 'Anonymous User')}</div>
                    <div class="card-info">
                        <div class="card-rating">
                            ${this.generateStars(review.rating || 0)}
                            <span class="rating-text">(${review.rating || 0}/5)</span>
                        </div>
                        <div class="review-comment">
                            "${this.escapeHtml(review.comments || 'No comment provided')}"
                        </div>
                    </div>
                </div>
            </div>
        `).join('');
    }

    setupScrollAnimations() {
        // Setup hover pause functionality for all scrolling lists
        const scrollingLists = [
            document.getElementById('doctorsList'),
            document.getElementById('hospitalsList'),
            document.getElementById('reviewsList')
        ];

        scrollingLists.forEach(list => {
            if (list) {
                // Pause animation on hover
                list.addEventListener('mouseenter', () => {
                    list.style.animationPlayState = 'paused';
                });

                // Resume animation when mouse leaves
                list.addEventListener('mouseleave', () => {
                    list.style.animationPlayState = 'running';
                });

                // Also pause when individual cards are hovered
                const cards = list.querySelectorAll('.doctor-card, .hospital-card, .review-card');
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
                        ${this.escapeHtml(doctorDetails.doctorType || 'General Practitioner')}
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

    setupEventListeners() {
        // Customization Modal
        const customizeBtn = document.getElementById('customizeRequestBtn');
        const customizationModal = document.getElementById('customizationModal');
        const closeCustomizationModal = document.getElementById('closeCustomizationModal');

        if (customizeBtn && customizationModal) {
            customizeBtn.addEventListener('click', (e) => {
                e.preventDefault();
                customizationModal.style.display = 'block';
            });
        }

        if (closeCustomizationModal) {
            closeCustomizationModal.addEventListener('click', () => {
                customizationModal.style.display = 'none';
            });
        }

        // Country change handler
        const countrySelect = document.getElementById('countrySelect');
        if (countrySelect) {
            countrySelect.addEventListener('change', (e) => {
                const selectedCountry = e.target.value;
                if (selectedCountry) {
                    this.loadStates(selectedCountry);
                } else {
                    const stateSelect = document.getElementById('stateSelect');
                    stateSelect.innerHTML = '<option value="">Choose a state...</option>';
                    stateSelect.disabled = true;
                }
            });
        }

        // Form submission
        const customizationForm = document.getElementById('customizationForm');
        if (customizationForm) {
            customizationForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitCustomizationForm();
            });
        }

        // Close error modal functionality
        const modal = document.getElementById('errorModal');
        const closeBtn = document.getElementById('closeErrorModal');

        if (closeBtn) {
            closeBtn.onclick = () => {
                if (modal) modal.style.display = 'none';
            };
        }

        // Close doctor details modal functionality
        const doctorModal = document.getElementById('doctorDetailsModal');
        const closeDoctorBtn = document.querySelector('.close-doctor-details');

        if (closeDoctorBtn) {
            closeDoctorBtn.onclick = () => {
                if (doctorModal) doctorModal.style.display = 'none';
            };
        }

        // Close modals when clicking outside
        window.onclick = (event) => {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
            if (event.target === doctorModal) {
                doctorModal.style.display = 'none';
            }
            if (event.target === customizationModal) {
                customizationModal.style.display = 'none';
            }
        };

        // Quick action button handlers
        document.addEventListener('click', (e) => {
            const actionBtn = e.target.closest('.action-btn');
            if (actionBtn) {
                const actionText = actionBtn.querySelector('span').textContent;
                this.handleQuickAction(actionText);
            }
        });

        // Refresh data every 5 minutes
        setInterval(() => {
            this.refreshDashboard();
        }, 300000); // 5 minutes
    }

    // UPDATED: New submitCustomizationForm method with redirect logic
    async submitCustomizationForm() {
        // Collect form values
        const country = document.getElementById('countrySelect').value;
        const state = document.getElementById('stateSelect').value;
        const hospitalType = document.getElementById('hospitalTypeSelect').value;
        const facility = document.getElementById('facilitySelect').value;

        // Validate required fields
        if (!country || !state || !hospitalType || !facility) {
            this.showError('Please fill in all required fields.');
            return;
        }

        // Create the customData map (as key-value pairs for URL params)
        const customData = new Map();
        customData.set('country', country);
        customData.set('state', state);
        customData.set('countryName', country); // Backend expects this key
        customData.set('specialisation', facility); // Using facility as specialisation

        // Convert Map to URL parameters
        const urlParams = new URLSearchParams();
        customData.forEach((value, key) => {
            urlParams.append(key, value);
        });

        try {
            // Determine redirect URL based on hospital type
            let redirectUrl;
            if (hospitalType.toLowerCase() === 'private') {
                redirectUrl = `/redirectPrivateHospital?${urlParams.toString()}`;
            } else if (hospitalType.toLowerCase() === 'government') {
                redirectUrl = `/redirectGovHospital?${urlParams.toString()}`;
            } else {
                this.showError('Invalid hospital type selected.');
                return;
            }

            // Close modal first
            document.getElementById('customizationModal').style.display = 'none';

            // Show loading indication (optional)
            console.log('Redirecting to:', redirectUrl);

            // Redirect to the appropriate dashboard
            window.location.href = redirectUrl;

        } catch (error) {
            console.error('Error during form submission:', error);
            this.showError('An error occurred while processing your request. Please try again.');
        }
    }

    handleQuickAction(actionText) {
        console.log('Quick action clicked:', actionText);
        switch(actionText) {
            case 'Find Doctor':
                // window.location.href = '/find-doctors';
                break;
            case 'Find Hospital':
                // window.location.href = '/find-hospitals';
                break;
            case 'Book Appointment':
                // window.location.href = '/book-appointment';
                break;
            case 'My Prescriptions':
                // window.location.href = '/prescriptions';
                break;
        }
    }

    async refreshDashboard() {
        try {
            await this.loadDashboardData();
            console.log('Dashboard refreshed successfully');
        } catch (error) {
            console.error('Failed to refresh dashboard:', error);
        }
    }

    generateStars(rating) {
        const fullStars = Math.floor(rating);
        const hasHalfStar = rating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        let starsHtml = '<div class="stars">';

        // Full stars
        for (let i = 0; i < fullStars; i++) {
            starsHtml += '<i class="fas fa-star star"></i>';
        }

        // Half star
        if (hasHalfStar) {
            starsHtml += '<i class="fas fa-star-half-alt star"></i>';
        }

        // Empty stars
        for (let i = 0; i < emptyStars; i++) {
            starsHtml += '<i class="far fa-star star empty"></i>';
        }

        starsHtml += '</div>';
        return starsHtml;
    }

    getInitials(name) {
        if (!name || name === 'Anonymous') return 'A';
        return name.split(' ')
                  .map(word => word.charAt(0))
                  .join('')
                  .substring(0, 2)
                  .toUpperCase();
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
            'hospitalsLoading',
            'reviewsLoading'
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
            // Show loading state
            content.innerHTML = `
                <div class="loading">
                    <i class="fas fa-spinner fa-spin"></i> Loading doctor details...
                </div>
            `;

            modal.style.display = 'block';

            // Load doctor details
            this.loadDoctorDetails(doctorName);
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
let dashboardInstance = null;

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    dashboardInstance = new PatientDashboard();
});

// Global function to handle "Get More Details" button clicks
window.handleGetDetails = function(type, index) {
    console.log(`Get details clicked for ${type} at index ${index}`);

    switch(type) {
        case 'doctor':
            if (dashboardInstance && dashboardInstance.doctorsData[index]) {
                const doctor = dashboardInstance.doctorsData[index];
                dashboardInstance.showDoctorDetailsModal(doctor.doctorName);
            } else {
                alert('Doctor information not available');
            }
            break;
        case 'hospital':
            // Navigate to hospital details page
            // window.location.href = `/hospital-details/${index}`;
            alert(`Viewing details for hospital at index ${index}`);
            break;
        case 'review':
            // Review cards no longer have "Get More Details" button
            console.log('Review details not implemented');
            break;
        default:
            console.log('Unknown detail type');
    }
};

// Handle page visibility changes to refresh data when user returns
document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
        // Page became visible, refresh data
        setTimeout(() => {
            if (dashboardInstance) {
                dashboardInstance.refreshDashboard();
            }
        }, 1000);
    }
});