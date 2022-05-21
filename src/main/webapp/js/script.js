function cleanElement(el) {
    while (el.firstChild) {
        el.removeChild(el.firstChild);
    }
}

function showBankStatements() {
    const d = document.getElementById("data");
    cleanElement(d);
    BankStatements.getAll()
            .then(data => {
                const d = document.getElementById("data");
                cleanElement(d);
                d.appendChild(document.createElement("br"));
                let bBack = document.createElement("button");
                bBack.addEventListener("click", () => {
                    showZmones();
                });
                bBack.appendChild(document.createTextNode("Back"));
                d.appendChild(bBack);
                const h3 = document.createElement("h3");
                h3.appendChild(document.createTextNode(`${event.target.zmogus.vardas} ${event.target.zmogus.pavarde} kontaktu sarasas`));
                d.appendChild(h3);
                let bAdd = document.createElement("button");
                bAdd.addEventListener("click", () => {
                    formKontaktas(event.target.zmogus);
                });
                bAdd.appendChild(document.createTextNode("Add"));
                d.appendChild(bAdd);
                const table = document.createElement("table");
                let tr, th, td, button;
                // creating table header
                tr = document.createElement("tr");
                th = document.createElement("th");
                th.appendChild(document.createTextNode("id"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Tipas"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Kontaktas"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Veiksmai"));
                tr.appendChild(th);
                table.appendChild(tr);
                // creating data table
                for (const row of data) {
                    tr = document.createElement("tr");
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.id));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode((row.tipas) ? row.tipas : ""));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode((row.kontaktas) ? row.kontaktas : ""));
                    tr.appendChild(td);
                    // creating action buttons for each row
                    td = document.createElement("td");
                    button = document.createElement("button");
                    button.appendChild(document.createTextNode("Edit"));
                    button.zmogus = row.zmogus;
                    button.kontaktasId = row.id;
                    button.addEventListener("click", showKontaktas);
                    td.appendChild(button);
                    button = document.createElement("button");
                    button.zmogus = row.zmogus;
                    button.kontaktasId = row.id;
                    button.appendChild(document.createTextNode("Delete"));
                    button.addEventListener("click", deleteKontaktas);
                    td.appendChild(button);
                    tr.appendChild(td);
                    table.appendChild(tr);
                }
                d.appendChild(table);
            })
            .catch(err => {
                alert("Failed to load list: " + err.message);
            });
}


