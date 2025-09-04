// Patient Dashboard JavaScript with Enhanced Features
class PatientDashboard {
    constructor() {
        this.apiEndpoint = '/dashboard/patient';
        this.doctorDetailsEndpoint = '/doctor/getDoctorDetails';
        this.customApiEndpoint = '/myCustom';
        this.contactApiEndpoint = '/contactUs';
        this.appointmentApiEndpoint = '/appointment';
        this.doctorsData = [];
        this.hospitalsData = [];
        this.currentContactFormType = '';
        this.currentAppointmentData = null;
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

    async loadDashboardData() {
        try {
            const response = await fetch(this.apiEndpoint);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            this.updateWelcomeUser(data.currentUser);
            this.updateLocationDetails(data.locationDetails);

            this.doctorsData = data.top5Doctors || [];
            this.hospitalsData = data.top5Hospitals || [];

            this.renderDoctorsList(data.top5Doctors);
            this.renderHospitalsList(data.top5Hospitals);
            this.renderReviewsList(data.top10RattingComment);
            this.setupScrollAnimations();

        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Unable to load dashboard data. Please check your connection and try again.');
            this.hideLoadingStates();
        }
    }

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
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

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
            const stateSelect = document.getElementById('stateSelect');
            stateSelect.innerHTML = '<option value="">Loading states...</option>';
            stateSelect.disabled = true;

            const response = await fetch(`${this.customApiEndpoint}/getStateList/${encodeURIComponent(countryName)}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const states = await response.json();
            stateSelect.innerHTML = '<option value="">Choose a state...</option>';

            if (!states || states.length === 0) {
                stateSelect.innerHTML = '<option value="">No states found</option>';
                stateSelect.disabled = true;
                return;
            }

            states.forEach(state => {
                const option = document.createElement('option');
                option.value = state.stateName || state.name || state;
                option.textContent = state.stateName || state.name || state;
                stateSelect.appendChild(option);
            });

            stateSelect.disabled = false;

        } catch (error) {
            console.error('Error loading states:', error);
            const stateSelect = document.getElementById('stateSelect');
            stateSelect.innerHTML = '<option value="">Failed to load states</option>';
            stateSelect.disabled = true;
            this.showError('Failed to load states. Please try again.');
        }
    }

    async loadHospitalTypes() {
        try {
            const response = await fetch(`${this.customApiEndpoint}/hospitalType`);
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

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
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

            const facilities = await response.json();

            // Load facilities for customization form
            const facilitySelect = document.getElementById('facilitySelect');
            facilitySelect.innerHTML = '<option value="">Choose a facility...</option>';

            // Load facilities for appointment form
            const appointmentFacilitySelect = document.getElementById('appointmentFacilityName');
            appointmentFacilitySelect.innerHTML = '<option value="">Select a facility...</option>';

            facilities.forEach(facility => {
                const option1 = document.createElement('option');
                option1.value = facility.facilityName;
                option1.textContent = `${facility.facilityName} - ${facility.facilityDescription}`;
                facilitySelect.appendChild(option1);

                const option2 = document.createElement('option');
                option2.value = facility.facilityName;
                option2.textContent = `${facility.facilityName} - ${facility.facilityDescription}`;
                appointmentFacilitySelect.appendChild(option2);
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
                    <div class="card-buttons">
                        <button class="get-details-btn" onclick="handleGetDetails('doctor', ${index % doctors.length})">
                            Details
                        </button>
                        <button class="book-appointment-btn" onclick="handleBookAppointment('doctor', ${index % doctors.length})">
                            Book
                        </button>
                    </div>
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
                    <div class="card-buttons">
                        <button class="get-details-btn" onclick="handleGetDetails('hospital', ${index % hospitals.length})">
                            Details
                        </button>
                        <button class="book-appointment-btn" onclick="handleBookAppointment('hospital', ${index % hospitals.length})">
                            Book
                        </button>
                    </div>
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
        const scrollingLists = [
            document.getElementById('doctorsList'),
            document.getElementById('hospitalsList'),
            document.getElementById('reviewsList')
        ];

        scrollingLists.forEach(list => {
            if (list) {
                list.addEventListener('mouseenter', () => {
                    list.style.animationPlayState = 'paused';
                });

                list.addEventListener('mouseleave', () => {
                    list.style.animationPlayState = 'running';
                });

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
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

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

        // Contact Us Modal
        const contactUsBtn = document.getElementById('contactUsBtn');
        const contactUsModal = document.getElementById('contactUsModal');
        const closeContactUsModal = document.getElementById('closeContactUsModal');

        if (contactUsBtn && contactUsModal) {
            contactUsBtn.addEventListener('click', (e) => {
                e.preventDefault();
                contactUsModal.style.display = 'block';
            });
        }

        if (closeContactUsModal) {
            closeContactUsModal.addEventListener('click', () => {
                contactUsModal.style.display = 'none';
            });
        }

        // Contact Form Modal
        const contactFormModal = document.getElementById('contactFormModal');
        const closeContactFormModal = document.getElementById('closeContactFormModal');

        if (closeContactFormModal) {
            closeContactFormModal.addEventListener('click', () => {
                contactFormModal.style.display = 'none';
            });
        }

        // Appointment Modal
        const appointmentModal = document.getElementById('appointmentModal');
        const closeAppointmentModal = document.getElementById('closeAppointmentModal');

        if (closeAppointmentModal) {
            closeAppointmentModal.addEventListener('click', () => {
                appointmentModal.style.display = 'none';
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

        // Form submissions
        const customizationForm = document.getElementById('customizationForm');
        if (customizationForm) {
            customizationForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitCustomizationForm();
            });
        }

        const dynamicContactForm = document.getElementById('dynamicContactForm');
        if (dynamicContactForm) {
            dynamicContactForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitContactForm();
            });
        }

        const appointmentForm = document.getElementById('appointmentForm');
        if (appointmentForm) {
            appointmentForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.submitAppointmentForm();
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
            if (event.target === contactUsModal) {
                contactUsModal.style.display = 'none';
            }
            if (event.target === contactFormModal) {
                contactFormModal.style.display = 'none';
            }
            if (event.target === appointmentModal) {
                appointmentModal.style.display = 'none';
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
        }, 300000);
    }

    async submitCustomizationForm() {
        const country = document.getElementById('countrySelect').value;
        const state = document.getElementById('stateSelect').value;
        const hospitalType = document.getElementById('hospitalTypeSelect').value;
        const facility = document.getElementById('facilitySelect').value;

        if (!country || !state || !hospitalType || !facility) {
            this.showError('Please fill in all required fields.');
            return;
        }

        const customData = new Map();
        customData.set('country', country);
        customData.set('state', state);
        customData.set('countryName', country);
        customData.set('specialisation', facility);

        const urlParams = new URLSearchParams();
        customData.forEach((value, key) => {
            urlParams.append(key, value);
        });

        try {
            let redirectUrl;
            if (hospitalType.toLowerCase() === 'private') {
                redirectUrl = `/redirectPrivateHospital?${urlParams.toString()}`;
            } else if (hospitalType.toLowerCase() === 'government') {
                redirectUrl = `/redirectGovHospital?${urlParams.toString()}`;
            } else {
                this.showError('Invalid hospital type selected.');
                return;
            }

            document.getElementById('customizationModal').style.display = 'none';
            console.log('Redirecting to:', redirectUrl);
            window.location.href = redirectUrl;

        } catch (error) {
            console.error('Error during form submission:', error);
            this.showError('An error occurred while processing your request. Please try again.');
        }
    }

    showContactForm(formType) {
        this.currentContactFormType = formType;
        const contactFormModal = document.getElementById('contactFormModal');
        const contactFormTitle = document.getElementById('contactFormTitle');
        const contactFormContent = document.getElementById('contactFormContent');

        // Hide contact us modal
        document.getElementById('contactUsModal').style.display = 'none';

        let title = '';
        let formHtml = '';

        switch(formType) {
            case 'doctorRegistration':
                title = '<i class="fas fa-user-md"></i> Doctor Registration Request';
                formHtml = this.getDoctorRegistrationForm();
                break;
            case 'doctorRemoval':
                title = '<i class="fas fa-user-minus"></i> Doctor Removal Request';
                formHtml = this.getDoctorRemovalForm();
                break;
            case 'hospitalRegistration':
                title = '<i class="fas fa-hospital"></i> Hospital Registration Request';
                formHtml = this.getHospitalRegistrationForm();
                break;
            case 'hospitalRemoval':
                title = '<i class="fas fa-hospital-user"></i> Hospital Removal Request';
                formHtml = this.getHospitalRemovalForm();
                break;
        }

        contactFormTitle.innerHTML = title;
        contactFormContent.innerHTML = formHtml;
        contactFormModal.style.display = 'block';
    }

    getDoctorRegistrationForm() {
        return `
            <div class="form-group">
                <label class="form-label" for="doctorName">Doctor Name *</label>
                <input type="text" id="doctorName" name="doctorName" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorAge">Age *</label>
                <input type="number" id="doctorAge" name="doctorAge" class="form-input" min="25" max="80" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorGender">Gender *</label>
                <select id="doctorGender" name="doctorGender" class="form-select" required>
                    <option value="">Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                </select>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorYearsOfExperience">Years of Experience *</label>
                <input type="number" id="doctorYearsOfExperience" name="doctorYearsOfExperience" class="form-input" min="0" max="50" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorGraduateCollege">Graduate College *</label>
                <input type="text" id="doctorGraduateCollege" name="doctorGraduateCollege" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorFieldOfExpertise">Field of Expertise *</label>
                <input type="text" id="doctorFieldOfExpertise" name="doctorFieldOfExpertise" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorEmail">Email *</label>
                <input type="email" id="doctorEmail" name="doctorEmail" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorMobile">Mobile Number *</label>
                <input type="tel" id="doctorMobile" name="doctorMobile" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorDetailAddress">Address *</label>
                <textarea id="doctorDetailAddress" name="doctorDetailAddress" class="form-textarea" required></textarea>
            </div>
            <div class="form-group">
                <label class="form-label" for="doctorType">Doctor Type *</label>
                <select id="doctorType" name="doctorType" class="form-select" required>
                    <option value="">Select Type</option>
                    <option value="government">Government</option>
                    <option value="private">Private</option>
                </select>
            </div>
            <div class="form-group">
                <label class="form-label" for="hospitalAppliedFor">Hospital Applied For *</label>
                <input type="text" id="hospitalAppliedFor" name="hospitalAppliedFor" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="countryName">Country you want to work in*</label>
                <input type="text" id="countryName" name="countryName" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="stateName">State you want to work in*</label>
                <input type="text" id="stateName" name="stateName" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="facilityNames">Facility Names (comma-separated)</label>
                <input type="text" id="facilityNames" name="facilityNames" class="form-input" placeholder="e.g. Cardiology, Neurology">
            </div>
        `;
    }

    getDoctorRemovalForm() {
        return `
            <div class="form-group">
                <label class="form-label" for="doctorEmail">Doctor Email *</label>
                <input type="email" id="doctorEmail" name="doctorEmail" class="form-input" required>
                <small style="color: #64748b; font-size: 0.85rem;">Enter the email address of the doctor to be removed</small>
            </div>
        `;
    }

    getHospitalRegistrationForm() {
        return `
            <div class="form-group">
                <label class="form-label" for="hospitalName">Hospital Name *</label>
                <input type="text" id="hospitalName" name="hospitalName" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="hospitalType">Hospital Type *</label>
                <select id="hospitalType" name="hospitalType" class="form-select" required>
                    <option value="">Select Type</option>
                    <option value="government">Government</option>
                    <option value="private">Private</option>
                </select>
            </div>
            <div class="form-group">
                <label class="form-label" for="hospitalAddress">Hospital Address *</label>
                <textarea id="hospitalAddress" name="hospitalAddress" class="form-textarea" required></textarea>
            </div>
            <div class="form-group">
                <label class="form-label" for="hospitalEmail">Contact Email *</label>
                <input type="email" id="hospitalEmail" name="hospitalEmail" class="form-input" required>
            </div>
            <div class="form-group">
                <label class="form-label" for="hospitalPhone">Contact Phone *</label>
                <input type="tel" id="hospitalPhone" name="hospitalPhone" class="form-input" required>
            </div>
        `;
    }

    getHospitalRemovalForm() {
        return `
            <div class="form-group">
                <label class="form-label" for="hospitalName">Hospital Name *</label>
                <input type="text" id="hospitalName" name="hospitalName" class="form-input" required>
                <small style="color: #64748b; font-size: 0.85rem;">Enter the name of the hospital to be removed</small>
            </div>
        `;
    }

    async submitContactForm() {
        const form = document.getElementById('dynamicContactForm');
        const formData = new FormData(form);
        const data = {};

        // Convert FormData to regular object
        for (let [key, value] of formData.entries()) {
            if (key === 'facilityNames' && value) {
                // Convert comma-separated string to array
                data[key] = value.split(',').map(item => item.trim()).filter(item => item.length > 0);
            } else {
                data[key] = value;
            }
        }

        let endpoint = '';
        let requestBody = null;

        switch(this.currentContactFormType) {
            case 'doctorRegistration':
                endpoint = `${this.contactApiEndpoint}/doctorRegistrationRequest`;
                requestBody = data;
                break;
            case 'doctorRemoval':
                endpoint = `${this.contactApiEndpoint}/doctorRemovalRequest`;
                requestBody = data.doctorEmail;
                break;
            case 'hospitalRegistration':
                endpoint = `${this.contactApiEndpoint}/hospitalRegistrationRequest`;
                requestBody = data;
                break;
            case 'hospitalRemoval':
                endpoint = `${this.contactApiEndpoint}/hospitalRemovalRequest`;
                requestBody = data.hospitalName;
                break;
        }

        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody)
            });

            if (response.status === 200) {
                this.showNotification('Request submitted successfully!', 'success');
                document.getElementById('contactFormModal').style.display = 'none';

                // Auto logout after 10 seconds for successful requests
                setTimeout(() => {
                    this.showNotification('Logging out...', 'info');
                    setTimeout(() => {
                        window.location.href = '/logout';
                    }, 2000);
                }, 10000);
            } else {
                const errorMessage = await response.text();
                this.showError(errorMessage || 'Request failed. Please try again.');
            }
        } catch (error) {
            console.error('Error submitting contact form:', error);
            this.showError('Network error. Please check your connection and try again.');
        }
    }

    showAppointmentModal(type, index) {
        const appointmentModal = document.getElementById('appointmentModal');
        const doctorNameField = document.getElementById('appointmentDoctorName');
        const hospitalNameField = document.getElementById('appointmentHospitalName');

        let doctorName = '';
        let hospitalName = '';

        if (type === 'doctor' && this.doctorsData[index]) {
            doctorName = this.doctorsData[index].doctorName || '';
            hospitalName = this.doctorsData[index].hospitalName || '';
        } else if (type === 'hospital' && this.hospitalsData[index]) {
            hospitalName = this.hospitalsData[index].hospitalName || '';
            // For hospital booking, we might need to select a doctor later
            doctorName = 'To be assigned';
        }

        doctorNameField.value = doctorName;
        hospitalNameField.value = hospitalName;

        // Store current appointment data for submission
        this.currentAppointmentData = {
            type: type,
            index: index,
            doctorName: doctorName,
            hospitalName: hospitalName
        };

        appointmentModal.style.display = 'block';
    }

    async submitAppointmentForm() {
        const facilityName = document.getElementById('appointmentFacilityName').value;
        const dateTime = document.getElementById('appointmentDateTime').value;

        if (!facilityName || !dateTime) {
            this.showError('Please fill in all required fields.');
            return;
        }

        if (!this.currentAppointmentData) {
            this.showError('Appointment data not found. Please try again.');
            return;
        }

        const appointmentData = {
            doctorName: this.currentAppointmentData.doctorName,
            hospitalName: this.currentAppointmentData.hospitalName,
            facilityName: facilityName,
            localDateTime: dateTime
        };

        try {
            const response = await fetch(`${this.appointmentApiEndpoint}/register`, {
                method: 'GET', // Note: Using GET as specified in requirements
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(appointmentData)
            });

            if (response.status === 200) {
                this.showNotification('Appointment booked successfully!', 'success');
                document.getElementById('appointmentModal').style.display = 'none';

                // Reset form
                document.getElementById('appointmentForm').reset();
                this.currentAppointmentData = null;
            } else {
                const errorMessage = await response.text();
                this.showError(errorMessage || 'Failed to book appointment. Please try again.');
            }
        } catch (error) {
            console.error('Error booking appointment:', error);
            this.showError('Network error. Please check your connection and try again.');
        }
    }

    handleQuickAction(actionText) {
        console.log('Quick action clicked:', actionText);
        switch(actionText) {
            case 'Find Doctor':
                // Implementation for find doctor
                break;
            case 'Find Hospital':
                // Implementation for find hospital
                break;
            case 'Book Appointment':
                this.showNotification('Please use the "Book" button on doctor or hospital cards to book appointments.', 'info');
                break;
            case 'My Prescriptions':
                // Implementation for prescriptions
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

    showNotification(message, type = 'success') {
        const notification = document.getElementById('notification');
        const notificationMessage = document.getElementById('notificationMessage');

        if (notification && notificationMessage) {
            notificationMessage.textContent = message;
            notification.className = `notification ${type} show`;

            setTimeout(() => {
                notification.classList.remove('show');
            }, 5000);
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

    escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Global reference to dashboard instance
let dashboardInstance = null;

// Global functions for button handlers
window.showContactForm = function(formType) {
    if (dashboardInstance) {
        dashboardInstance.showContactForm(formType);
    }
};

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
            alert(`Viewing details for hospital at index ${index}`);
            break;
        default:
            console.log('Unknown detail type');
    }
};

window.handleBookAppointment = function(type, index) {
    console.log(`Book appointment clicked for ${type} at index ${index}`);

    if (dashboardInstance) {
        dashboardInstance.showAppointmentModal(type, index);
    } else {
        alert('Dashboard not initialized');
    }
};

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    dashboardInstance = new PatientDashboard();
});

// Handle page visibility changes to refresh data when user returns
document.addEventListener('visibilitychange', () => {
    if (!document.hidden) {
        setTimeout(() => {
            if (dashboardInstance) {
                dashboardInstance.refreshDashboard();
            }
        }, 1000);
    }
});