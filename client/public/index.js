/** @typedef {{load: (Promise<unknown>); flags: (unknown)}} ElmPagesInit */

/** @type ElmPagesInit */
export default {
    load: async function (elmLoaded) {
        window.history.replaceState(null, document.title, window.location.pathname.split('index.html')[0] + window.location.search)
        const app = await elmLoaded;
    },
    flags: function () {
        return {
            apiRoot: window.location.port === "1234" ? "http://localhost:3000" : "",
        }
    },
};
