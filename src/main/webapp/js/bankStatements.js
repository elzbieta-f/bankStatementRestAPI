/**
 * Class holds collection of static methods to communicate
 * with server web services to work with 'bankStatement' entities.
 */
class BankStatements {
    /**
     * Holds url for web service.
     *
     * @type {string}
     */
    static _url = "./ws/bankStatement/";

    /**
     * Fetches list of all bank statements.
     *
     * @returns {Promise<object[]>} list of entities
     * @throws {Error} when server returns unknown status
     */
    static async getAll() {
        const res = await fetch(BankStatements._url);
        if (res.status === 200) {
            return res.json();
        }
        throw new Error(`Unknown status: ${res.statusText} (${res.status})`)
    }
}


