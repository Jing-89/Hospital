const NURSE_API = "http://localhost:8080/api/nurses";
const STATION_API = "http://localhost:8080/api/stations";
let assignedStationIds = [];
let originalStationIds = [];

// 處理時間格式
function formatDateTime(dateString) {
    if (!dateString) return "";
    const date = new Date(dateString);
    const pad = (n) => n.toString().padStart(2, '0');
    return (
        date.getFullYear() + "/" +
        pad(date.getMonth() + 1) + "/" +
        pad(date.getDate()) + " " +
        pad(date.getHours()) + ":" +
        pad(date.getMinutes()) + ":" +
        pad(date.getSeconds())
    );
}


// ======= 站點管理 =======
function loadStations(targetSelectId) {
    $.get(STATION_API, function (stations) {
        const select = $(`#${targetSelectId}`);
        select.empty();
        stations.forEach(s => select.append(`<option value="${s.id}">${s.name}</option>`));
    });
}

function loadStationList() {
    $.get(STATION_API, function (stations) {
        const tbody = $("#stationTable tbody");
        tbody.empty();
        stations.forEach(s => {

            tbody.append(`
            <tr>
              <td>${s.name}</td>
              <td>${formatDateTime(s.updatedAt) || ""}</td>
              <td>
                <button class="btn btn-sm btn-info" onclick="editStation(${s.id})">View</button>
                <button class="btn btn-sm btn-danger" onclick="deleteStation(${s.id})">Del</button>
              </td>
            </tr>
          `);
        });
    });
}

$("#createStationForm").on("submit", function (e) {
    e.preventDefault();

    const station = { name: $("#stationNameInput").val() };

    $.ajax({
        url: STATION_API,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(station),
        success: function () {
            $("#createStationModal").modal("hide");
            loadStations("availableStationsNurse");
            loadStationList();
        },
        error: function (xhr, status, error) {
            alert("錯誤: " + error);
        }
    });
});

function editStation(id) {
    $("#editStationModal .modal-title").text("編輯站點");

    $.get(`${STATION_API}/${id}`, function (station) {
        console.log(station);

        $("#editStationNameInput").val(station.name);

        $("#editStationModal").modal("show");

        const tbody = $("#stationNursesTable tbody");
        tbody.empty();

        if (station.nurses && station.nurses.length > 0) {
            station.nurses.forEach(nurse => {
                tbody.append(`
                    <tr>
                        <td>${nurse.number}</td>
                        <td>${formatDateTime(nurse.assignedAt) || ""}</td>
                    </tr>
                `);
            });
        } else {
            tbody.append("<tr><td colspan='2'>無護士資料</td></tr>");
        }
    });

    // 編輯站點表單
    $("#editStationModal").off("submit").on("submit", function (e) {
        e.preventDefault();

        const station = {
            name: $("#editStationNameInput").val(),
        };

        $.ajax({
            url: `${STATION_API}/${id}`,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(station),
            success: function (response) {
                $("#editStationModal").modal("hide");
                loadStationList();
            },
            error: function (xhr, status, error) {
                alert("更新失敗：" + error);
            }
        });
    });
}

function deleteStation(id) {
    if (!confirm("確定要刪除這個站點嗎？")) return;
    $.ajax({ url: `${STATION_API}/${id}`, type: "DELETE", success: loadStationList });
}

// ======= 護士管理 =======
function loadNurses() {

    $.get(NURSE_API, function (nurses) {


        const tbody = $("#nurseListTable tbody");
        tbody.empty();

        nurses.forEach(n => {


            tbody.append(`
            <tr>
              <td>${n.number}</td>
              <td>${formatDateTime(n.updatedAt) || ""}</td>
              <td>
                <button class="btn btn-sm btn-info" onclick="editNurse(${n.id})" data-bs-toggle="modal" data-bs-target="#createNurseModal">View</button>
                <button class="btn btn-sm btn-danger" onclick="deleteNurse(${n.id})">Del</button>
              </td>
            </tr>
          `);
        });
    }).fail(function (xhr, status, error) {
        console.error("載入護士清單失敗:", error);
    });
}

function createNurseModal() {
    $("#createNurseModal .modal-title").text("新增護士");
    $("#nurseIdInput").val("");
    $("#employeeNoInput").val("");
    $("#nurseNameInput").val("");
    $("#assignedStationsNurse").empty();
    assignedStationIds = [];
    originalStationIds = [];
    loadStations("availableStationsNurse");
}

// 編輯護士表單
function editNurse(id) {

    $("#createNurseModal .modal-title").text("編輯護士");

    $.get(`${NURSE_API}/${id}`, function (nurse) {

        $("#nurseIdInput").val(nurse.id); // 這部分不會顯示在畫面上
        $("#employeeNoInput").val(nurse.number);
        $("#nurseNameInput").val(nurse.name);

        $("#assignedStationsNurse").empty();
        assignedStationIds = [];
        originalStationIds = [];

        if (nurse.stations && nurse.stations.length > 0) {
            nurse.stations.forEach(s => {

                assignedStationIds.push(s.id);
                originalStationIds.push(s.id);

                $("#assignedStationsNurse").append(`<option value="${s.id}">${s.name}</option>`);
            });
        } else {
            console.log("護士沒有任何站點資料");
        }


        loadStations("availableStationsNurse");
    }).fail(function (xhr, status, error) {
        console.error("載入護士資料失敗:", error);
        console.error("錯誤詳情:", xhr.responseText);
    });
}

$("#assignBtnNurse").on("click", function () {
    $("#availableStationsNurse option:selected").each(function () {
        const id = Number($(this).val());
        const name = $(this).text();

        if (!assignedStationIds.includes(id)) {
            assignedStationIds.push(id);
            $("#assignedStationsNurse").append(`<option value="${id}">${name}</option>`);
        }
    });
});

$("#removeBtnNurse").on("click", function () {
    $("#assignedStationsNurse option:selected").each(function () {
        const id = Number($(this).val());
        const name = $(this).text();

        assignedStationIds = assignedStationIds.filter(sid => sid !== id);
        $(this).remove();

    });
});

// 處理站點指派/移除的函數
async function updateNurseStationAssignments(nurseId) {
    try {

        // 新增的站點
        const stationsToAssign = assignedStationIds.filter(id => !originalStationIds.includes(id));

        // 移除的站點
        const stationsToUnassign = originalStationIds.filter(id => !assignedStationIds.includes(id));

        for (const stationId of stationsToAssign) {

            const response = await $.ajax({
                url: `${STATION_API}/${stationId}/assign`,
                type: "PUT",
                contentType: "application/json",
                data: JSON.stringify({ nurseIds: [nurseId] })
            });

            console.log(`指派成功:`, response);
        }

        for (const stationId of stationsToUnassign) {


            const response = await $.ajax({
                url: `${STATION_API}/${stationId}/unassign`,
                type: "PUT",
                contentType: "application/json",
                data: JSON.stringify({ nurseIds: [nurseId] })
            });

            console.log(`移除成功:`, response);
        }

        return true;

    } catch (error) {
        console.error("更新站點指派時發生錯誤:", error);
        console.error("錯誤詳情:", error.responseText || error.responseJSON);
        throw error;
    }
}

// 護士表單提交處理
$("#createNurseForm").on("submit", async function (e) {
    e.preventDefault();

    const id = $("#nurseIdInput").val();
    const nurse = {
        number: $("#employeeNoInput").val(),
        name: $("#nurseNameInput").val()
    };

    try {
        let nurseId;

        if (id) {
            // 編輯模式：先更新站點分配，再更新基本資料
            await updateNurseStationAssignments(id);

            // 檢查護士資料是否需要更新
            const currentNurse = await $.get(`${NURSE_API}/${id}`);
            const needsUpdate = currentNurse.number !== nurse.number || currentNurse.name !== nurse.name;

            if (needsUpdate) {

                const response = await $.ajax({
                    url: `${NURSE_API}/${id}`,
                    type: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify(nurse)
                });
                console.log("基本資料更新完成:", response);
            } else {
                console.log("基本資料無需更新");
            }

            nurseId = id;
        } else {
            const response = await $.ajax({
                url: NURSE_API,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(nurse)
            });

            console.log("護士新增成功:", response);
            nurseId = response.id;

            // 新增站點分配
            if (assignedStationIds.length > 0) {
                for (const stationId of assignedStationIds) {

                    await $.ajax({
                        url: `${STATION_API}/${stationId}/assign`,
                        type: "PUT",
                        contentType: "application/json",
                        data: JSON.stringify({ nurseIds: [nurseId] })
                    });
                }
            }
        }

        $("#createNurseModal").modal("hide");
        loadNurses();


    } catch (error) {
        console.error("操作失敗:", error);
        console.error("錯誤回應:", error.responseText);
        alert("操作失敗: " + (error.responseText || error.message));
    }
});

function deleteNurse(id) {
    if (!confirm("確定要刪除這位護士嗎？")) return;
    $.ajax({ url: `${NURSE_API}/${id}`, type: "DELETE", success: loadNurses });
}

// ======= 初始載入 =======
$(document).ready(function () {
    loadNurses();
    loadStationList();
    loadStations("availableStationsNurse");

    $('#createStationModal').on('hidden.bs.modal', function () {
        $('#createStationForm')[0].reset();
    });

    $('#editStationModal').on('hidden.bs.modal', function () {
        $('#editStationForm')[0].reset();
        $('#stationNursesTable tbody').empty();
    });

    $('#createNurseModal').on('hidden.bs.modal', function () {
        $('#createNurseForm')[0].reset();
        $('#nurseIdInput').val("");
        $('#assignedStationsNurse').empty();
        assignedStationIds = [];
        originalStationIds = [];
    });
});