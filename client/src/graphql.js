import {checkResponseStatus} from './handlers'

export const fetchGraphQL = (query) => {
  var auth = JSON.parse(localStorage.auth)
  return fetch(`${process.env.VUE_APP_SERVER_URL}/graphql?query=${encodeURIComponent(query)}`, {
    method: "GET",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Authorization': auth.token_type + ' ' + auth.access_token
    }
  }).then(checkResponseStatus)
}
