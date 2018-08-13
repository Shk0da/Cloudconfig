const fs = require('fs');
const path = require('path');

module.exports = {
    parseVersion,
    root,
    isExternalLib
};

// return the version number from `build.gradle` file
function parseVersion() {
    let version = '';
    const buildGradle = fs.readFileSync('build.gradle', 'utf8');
    let lines = buildGradle.split('\n');
    for (let line = 0; line < lines.length; line++) {
        if (lines[line].startsWith('version')) {
            version = lines[line].split('=')[1].replace(/['"]/g, '').trim();
            if (version !== null) {
                return version;
            }
        }
    }
    if (version === null) {
        throw new Error('build.gradle is malformed. No version is defined');
    }
    return version;
}

const _root = path.resolve(__dirname, '..');

function root(args) {
    args = Array.prototype.slice.call(arguments, 0);
    return path.join.apply(path, [_root].concat(args));
}

function isExternalLib(module, check = /node_modules/) {
    const req = module.userRequest;
    if (typeof req !== 'string') {
        return false;
    }
    return req.search(check) >= 0;
}
