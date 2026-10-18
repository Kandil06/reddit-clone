import { useEffect, useState } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import api from '../api/axios'
import PostCard from '../components/PostCard'

export default function SearchResults() {
  const [params] = useSearchParams()
  const q = params.get('q') || ''
  const [results, setResults] = useState(null)

  useEffect(() => {
    if (q) api.get('/search', { params: { q } }).then(({ data }) => setResults(data))
  }, [q])

  if (!results) return <div className="container"><p className="muted">Searching...</p></div>

  return (
    <div className="container">
      <h2>Results for "{q}"</h2>

      <h3>Communities</h3>
      {results.communities.length === 0 && <p className="muted">No communities found.</p>}
      {results.communities.map((c) => (
        <div className="card" key={c.id}>
          <Link to={`/r/${c.name}`}><strong>r/{c.name}</strong></Link>
          <p className="muted">{c.memberCount} members</p>
        </div>
      ))}

      <h3>Posts</h3>
      {results.posts.length === 0 && <p className="muted">No posts found.</p>}
      {results.posts.map((p) => <PostCard key={p.id} post={p} />)}

      <h3>Users</h3>
      {results.users.length === 0 && <p className="muted">No users found.</p>}
      {results.users.map((u) => (
        <div className="card" key={u.id}>
          <Link to={`/u/${u.username}`}><strong>u/{u.username}</strong></Link>
        </div>
      ))}
    </div>
  )
}
