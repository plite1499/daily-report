document.addEventListener("DOMContentLoaded", () => {

    const message = /*[[${message}]]*/ '';
    if (message) {
        alert(message);
    }

    // 全選択チェック
    const checkAll = document.getElementById("checkAll");
    const items = document.querySelectorAll(".checkItem");
    checkAll.addEventListener("change", () => {
        items.forEach(item => item.checked = checkAll.checked);
    });

    // flatpickr
    flatpickr("#from", { enableTime: false, dateFormat: "Y-m-d" });
    flatpickr("#to", { enableTime: false, dateFormat: "Y-m-d" });

    // ページング
    const pageSize = 10;
    let currentPage = 1;
    const rows = Array.from(document.querySelectorAll("#reportTbody tr"));
    const totalPages = Math.ceil(rows.length / pageSize);

    function showPage(page) {
        currentPage = page;
        const start = (page - 1) * pageSize;
        const end = start + pageSize;
        

        rows.forEach((row, index) => {
            row.style.display = (index >= start && index < end) ? "" : "none";
        });

        renderPagination();
    }

    function renderPagination() {
        const container = document.getElementById("pagination");
        container.innerHTML = "";

        // 前ボタン
        const prevBtn = document.createElement("button");
        prevBtn.textContent = "<";
        prevBtn.disabled = currentPage === 1;
        prevBtn.addEventListener("click", () => showPage(currentPage - 1));
        container.appendChild(prevBtn);

        // ページ番号ボタン（最大5個表示）
        let startPage = Math.max(1, currentPage - 2);
        let endPage = Math.min(totalPages, currentPage + 2);

        for (let i = startPage; i <= endPage; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            if (i === currentPage) {
                btn.classList.add("active"); // CSS で強調
                btn.disabled = true;
            }
            btn.addEventListener("click", () => showPage(i));
            container.appendChild(btn);
        }

        // 次ボタン
        const nextBtn = document.createElement("button");
        nextBtn.textContent = ">";
        nextBtn.disabled = currentPage === totalPages;
        nextBtn.addEventListener("click", () => showPage(currentPage + 1));
        container.appendChild(nextBtn);
    }

    // 初期表示
    showPage(1);
});