document.addEventListener("DOMContentLoaded", function () {
    fetch("/dashboardDoctor")
        .then(response => {
            if (!response.ok) throw response;
            return response.json();
        })
        .then(data => {
            document.getElementById("doctorName").textContent = data.CurrentUserName;

            renderAppointments("pendingAppointments", data.PendingAppointments, "Approve", "Reject");
            renderAppointments("upcomingAppointments", data.UpComingAppointments, "View", null);
            renderAppointments("completedAppointments", data.completedAppointments, "View", null);
            renderAppointments("canceledAppointments", data.canceledAppointments, "View", null);
            renderHospitals("hospitalList", data.hospitalListForJobApply);
        })
        .catch(async (err) => {
            let message = await err.text();
            showPopup(message, "red");
        });
});

function renderAppointments(containerId, list, approveText, rejectText) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    if (!list || list.length === 0) {
        container.innerHTML = "<p>No records found</p>";
        return;
    }

    list.forEach(item => {
        let card = document.createElement("div");
        card.classList.add("card");
        card.innerHTML = `
            <h4>${item.userName}</h4>
            <p>Email: ${item.userEmail}</p>
            <p>Mobile: ${item.userMobile}</p>
            <p>Status: ${item.appointmentStatus}</p>
        `;

        if (approveText) {
            let approveBtn = document.createElement("button");
            approveBtn.classList.add("btn-approve");
            approveBtn.textContent = approveText;
            approveBtn.onclick = () => actionHandler("/appointment/approve", item);
            card.appendChild(approveBtn);
        }

        if (rejectText) {
            let rejectBtn = document.createElement("button");
            rejectBtn.classList.add("btn-reject");
            rejectBtn.textContent = rejectText;
            rejectBtn.onclick = () => actionHandler("/appointment/reject", item);
            card.appendChild(rejectBtn);
        }

        let detailsBtn = document.createElement("button");
        detailsBtn.classList.add("btn-details");
        detailsBtn.textContent = "Details";
        detailsBtn.onclick = () => showPopup(JSON.stringify(item, null, 2), "blue");

        card.appendChild(detailsBtn);
        container.appendChild(card);
    });
}

function renderHospitals(containerId, list) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    if (!list || list.length === 0) {
        container.innerHTML = "<p>No hospitals available</p>";
        return;
    }

    list.forEach(item => {
        let card = document.createElement("div");
        card.classList.add("card");
        card.innerHTML = `
            <h4>${item.hospitalName}</h4>
            <p>Type: ${item.hospitalType}</p>
            <p>Rating: ${item.hospitalRating}</p>
            <p>Address: ${item.hospitalAddress}</p>
        `;

        let applyBtn = document.createElement("button");
        applyBtn.classList.add("btn-approve");
        applyBtn.textContent = "Apply";
        applyBtn.onclick = () => actionHandler("/hospital/apply", item);

        card.appendChild(applyBtn);
        container.appendChild(card);
    });
}

function actionHandler(url, payload) {
    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (!response.ok) throw response;
            return response.text();
        })
        .then(message => {
            showPopup(message, "green");
        })
        .catch(async (err) => {
            let message = await err.text();
            showPopup(message, "red");
        });
}

function showPopup(message, color) {
    const popup = document.getElementById("popup");
    popup.textContent = message;
    popup.style.background = color;
    popup.style.display = "block";

    setTimeout(() => {
        popup.style.display = "none";
    }, 3000);
}
