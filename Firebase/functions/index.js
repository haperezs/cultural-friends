/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {defineSecret} = require("firebase-functions/params");
const {onRequest} = require("firebase-functions/v2/https");
const {Translate} = require("@google-cloud/translate").v2;

const API_KEY = defineSecret("API_KEY");

exports.translateText = onRequest(
    {secrets: [API_KEY]},
    async (req, res) => {
      const {text, source, target} = req.body;

      if (!text || !target) {
        return res.status(400).json({error: "Missing parameters"});
      }

      try {
        const translate = new Translate({
          key: API_KEY.value(),
        });

        const [translation] = await translate.translate(text, {
          from: source || "auto",
          to: target,
        });

        res.json({translatedText: translation});
      } catch (err) {
        console.error("Translation error:", err);
        res.status(500).json({error: "Translation failed"});
      }
    },
);
