function cleanElement(el) {
    while (el.firstChild) {
        el.removeChild(el.firstChild);
    }
}

const _url = "./ws/bs/";
/**
 * Fetches list of all bank statements.
 *
 * @returns {Promise<object[]>} list of entities
 * @throws {Error} when server returns unknown status
 */
async function getAll() {
    const res = await fetch(_url);
    if (res.status === 200) {
        return res.json();
    }
    throw new Error(`Unknown status: ${res.statusText} (${res.status})`)
}
/**
 * Fetches Array of all bank accounts
 *
 * @returns {Promise<object[]>} list of entities
 * @throws {Error} when server returns unknown status
 */

async function getAccounts() {
    const res = await fetch(_url + "accounts");
    if (res.status === 200) {
        return res.json();
    }
    throw new Error(`Unknown status: ${res.statusText} (${res.status})`)
}


/**
 * Puts the fetched result from getAll() method to a HTML table  * 
 */
function showBankStatements() {
    const d = document.getElementById("data");
    const f = document.getElementById("form");
    cleanElement(d);
    cleanElement(f);
    getAll()
            .then(data => {
                let h3 = document.createElement("h3");
                h3.appendChild(document.createTextNode("List of all bank statements"));
                d.appendChild(h3);
                const table = document.createElement("table");
                let tr, th, td, button;
                // creating table header
                tr = document.createElement("tr");
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Account number"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Operation date"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Beneficiary"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Comment"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Amount"));
                tr.appendChild(th);
                th = document.createElement("th");
                th.appendChild(document.createTextNode("Currency"));
                tr.appendChild(th);
                table.appendChild(tr);
                // creating data table
                for (const row of data) {
                    tr = document.createElement("tr");
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.accountNumber));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.operationDate));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.beneficiary));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.comment));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.amount));
                    tr.appendChild(td);
                    td = document.createElement("td");
                    td.appendChild(document.createTextNode(row.currency));
                    tr.appendChild(td);
                    table.appendChild(tr);
                }
                d.appendChild(table);
                h3 = document.createElement("h3");
                h3.appendChild(document.createTextNode("Export to CSV file by date:"));
                f.appendChild(h3);
                let form = document.createElement("form");
                form.action = "ws/bs";
                form.method = "POST";
                let label = document.createElement("label");
                label.for = "from";
                label.appendChild(document.createTextNode("Date from"));
                form.appendChild(label);
                let input = document.createElement("input");
                input.type = "date";
                input.name = "from";
                input.id = "from";
                form.appendChild(input);
                form.appendChild(document.createElement("br"));
                label = document.createElement("label");
                label.for = "to";
                label.appendChild(document.createTextNode("Date to"));
                form.appendChild(label);
                input = document.createElement("input");
                input.type = "date";
                input.name = "to";
                input.id = "to";
                form.appendChild(input);
                form.appendChild(document.createElement("br"));
                input = document.createElement("input");
                input.type = "submit";
                input.value = "Export to CSV";
                form.appendChild(input);
                f.appendChild(form);
            })
            .catch(err => {
                alert("Failed to load list: " + err.message);
            });
}

/**
 * Puts the fetched result from getAccounts() method to a HTML form  * 
 */
function calculateBalance() {
    const d = document.getElementById("data");
    const f = document.getElementById("form");
    cleanElement(d);
    cleanElement(f);
    getAccounts().
            then(data => {
                h3 = document.createElement("h3");
                h3.appendChild(document.createTextNode("Calculate balance for period of time:"));
                f.appendChild(h3);
                let label = document.createElement("label");
                label.for = "account";
                label.appendChild(document.createTextNode("Select account"));
                f.appendChild(label);
                let select = document.createElement("select");
                select.id = "account";
                select.name = "account";
                for (const el of data) {
                    let option = document.createElement("option");
                    option.value = el;
                    option.appendChild(document.createTextNode(el));
                    select.appendChild(option);
                }
                f.appendChild(select);
                f.appendChild(document.createElement("br"));
                label = document.createElement("label");
                label.for = "from";
                label.appendChild(document.createTextNode("Date from"));
                f.appendChild(label);
                let input = document.createElement("input");
                input.type = "date";
                input.name = "from";
                input.id = "from";
                f.appendChild(input);
                f.appendChild(document.createElement("br"));
                label = document.createElement("label");
                label.for = "to";
                label.appendChild(document.createTextNode("Date to"));
                f.appendChild(label);
                input = document.createElement("input");
                input.type = "date";
                input.name = "to";
                input.id = "to";
                f.appendChild(input);
                f.appendChild(document.createElement("br"));
                let bt = document.createElement("button");
                bt.appendChild(document.createTextNode("Calculate"));
                bt.addEventListener("click", () => {
                    let el = document.getElementById("account");
                    const account = el.options[el.selectedIndex].value;
                    let from = document.getElementById("from").value;
                    if (!from){
                        from="from";
                    }
                    let to = document.getElementById("from").value;
                    if (!to){
                        to="now";
                    }
                    let action = goTo(`${_url}accounts/${account}/${from}/${to}`);
                    action
                            .then(data => {
                                if (data) {
                                    showBalance();
                                } else {
                                    alert("Failed to load balance");
                                }
                            })
                            .catch(err => {
                                alert("Failed to load balance: " + err.message);
                            });
                });
                f.appendChild(bt);
            })
            .catch(err => {
                alert("Failed to load accounts list: " + err.message);
            });
}

async function goTo(loc)
{
    const res = await fetch(loc);
    if (res.status === 200) {
        return res.json();
    }
    throw new Error(`Unknown status: ${res.statusText} (${res.status})`)
}




function showBalance(data) {
    const d = document.getElementById("data");
    const f = document.getElementById("form");
    cleanElement(d);
    cleanElement(f);
    h3 = document.createElement("h3");
    h3.appendChild(document.createTextNode(`Balance for account ${data.accountNumber}: ${data.result} ${data.currency}`));
    d.appendChild(h3);
}




