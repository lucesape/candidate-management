/**
 * Created by blajv on 11.04.2017.
 */
var path = require('path');

module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: false,
    debug: true,
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        loaders: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                loader: 'babel',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015', 'react']
                }
            }
        ],
        plugins: [
            // ...
            function()
            {
                this.plugin("done", function(stats)
                {
                    if (stats.compilation.errors && stats.compilation.errors.length && process.argv.indexOf('--watch') == -1)
                    {
                        console.log(stats.compilation.errors);
                        process.exit(1); // or throw new Error('webpack build failed.');
                    }
                    // ...
                });
            }
            // ...
        ]
    }
};