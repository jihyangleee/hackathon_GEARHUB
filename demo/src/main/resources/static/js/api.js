// Simple helper to call the demo API with Authorization header from localStorage
const apiBase = '/api';
async function apiFetch(path, opts={}){
  opts.headers = opts.headers || {};
  const token = localStorage.getItem('accessToken');
  if(token) opts.headers['Authorization'] = 'Bearer ' + token;
  const res = await fetch(apiBase + path, opts);
  if(!res.ok){
    const txt = await res.text().catch(()=>res.statusText);
    throw new Error(txt || res.statusText);
  }
  return res.status === 204 ? null : res.json().catch(()=>null);
}
function apiGet(path){ return apiFetch(path, {method:'GET'}); }
function apiPost(path, body){ return apiFetch(path, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(body)}); }
